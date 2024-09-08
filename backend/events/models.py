from django.core.exceptions import ValidationError
from django.contrib.auth.models import User
from django.utils import timezone
from django.db import models
import logging

from notifications.views import firebase_send_message
from notifications.models import UserNotification


class Event(models.Model):
    class Meta:
        verbose_name = 'Мероприятие'
        verbose_name_plural = 'Мероприятия'
    
    def __str__(self) -> str:
        return str(self.name)

    def save(self, *args, **kwargs):
        self._send_message_to_users()
        # reset send push flag
        self.send_push = False
        super().save(*args,**kwargs)

    def clean(self):
        self._validate_start_and_end_time()
        return super().clean()

    name = models.CharField(
        verbose_name='Название',
        max_length=255,
    )
    description = models.TextField(
        verbose_name='Описание',
        blank=True,
    )
    start_time = models.DateTimeField(
        verbose_name='Время начала',
    )
    end_time = models.DateTimeField(
        verbose_name='Время окончания',
    )
    send_push = models.BooleanField(
        verbose_name='Отправить push',
        default=False, #pyright: ignore
        help_text='Для скрытых мероприятий push уведомления отключены'
    )
    attendees = models.ManyToManyField(
        User,
        verbose_name='Участники',
        blank=True,
    )
    is_registration_open = models.BooleanField(
        verbose_name='Регистрация',
        default=True, #pyright: ignore
        help_text='Внимание. Регистрация автоматически закрывается при открытии мероприятия'
    )
    hidden = models.BooleanField(
        verbose_name='Скрыть мероприятие',
        default=False, #pyright: ignore
    )


    def is_register_available(self) -> bool:
        # NOTE also need update logic in view and serializers
        now = timezone.localtime()
        start = self.start_time
        if not(start): return False
        return start > now and bool(self.is_registration_open)

    def is_active_now(self) -> bool:
        now = timezone.localtime()
        start = self.start_time
        end = self.end_time
        if not(start and end): return False
        return start <= now <= end

    def is_active_today(self) -> bool:
        now = timezone.localtime()
        start = self.start_time
        end = self.end_time
        if not(start and end): return False
        return start.date() <= now.date() <= end.date() #pyright: ignore

    def is_upcoming(self):
        now = timezone.localtime()
        start = self.start_time
        if not(start): return False
        return start > now

    def _send_message_to_users(self) -> None:
        title = f'Ура! Появилось новое мероприятие! {self.name}.'
        description = str(self.description)
        if not(self.send_push) or self.hidden: return
        users = UserNotification.objects.all() #pyright: ignore
        logging.warning(f'Send push messages, users count: {len(users)}')
        for user in users:
            firebase_send_message(
                token=user.firebase_push_hash,
                title=title,
                description=description,
            )

    def _validate_start_and_end_time(self) -> None:
        if self.start_time >= self.end_time:
            message = 'Invalid event range. Please, check start_time and end_time'
            raise ValidationError(message=message)
