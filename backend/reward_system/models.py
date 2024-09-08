from django.contrib.auth.models import User
from django.db import models


class UserRewardCouter(models.Model):
    class Meta:
        verbose_name = 'Счетчик'
        verbose_name_plural = 'Счетчики'
 
    user = models.OneToOneField(
        User, 
        on_delete=models.CASCADE,
        verbose_name='Пользователь'
    )
    event_counter = models.PositiveIntegerField(
        verbose_name='Количество регистраций на мероприятия', #pyright: ignore
        default=0, #pyright: ignore
    )
    books_recovered = models.PositiveIntegerField(
        verbose_name='Количество возвращенных книг', #pyright: ignore
        default=0, #pyright: ignore
    )
