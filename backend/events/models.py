from django.utils import timezone
from django.db import models


class Event(models.Model):
    class Meta:
        verbose_name = 'Мероприятие'
        verbose_name_plural = 'Мероприятия'
    
    def __str__(self) -> str:
        return str(self.name)


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
        now = timezone.now()
        start = self.start_time
        if not(start): return False
        return start > now and bool(self.is_registration_open)

    def is_active_now(self) -> bool:
        now = timezone.now()
        start = self.start_time
        end = self.end_time
        if not(start and end): return False
        return start <= now <= end

    def is_active_today(self) -> bool:
        now = timezone.now()
        start = self.start_time
        end = self.end_time
        if not(start and end): return False
        return start.date() <= now.date() <= end.date() #pyright: ignore

    def is_upcoming(self):
        now = timezone.now()
        start = self.start_time
        if not(start): return False
        return start > now
