from django.core.exceptions import ValidationError
from django.core.validators import (
    MinValueValidator,
    MaxValueValidator,
    MaxLengthValidator,
    MinLengthValidator,
)
from django.db.models.signals import post_delete
from django.contrib.auth.models import User
from django.dispatch import receiver
from django.db import models
from datetime import datetime


CERTIFICATE_NUMBER_LEN = 8


class Book(models.Model):
    class Meta:
        verbose_name = 'книга'
        verbose_name_plural = 'книги'

    def __str__(self) -> str:
        return str(self.name)

    def decrease_count(self, value:int=1,*,save:bool=True):
        self.count -= value #pyright: ignore
        if save: self.save()

    def increase_count(self,value:int=1,*,save:bool=True):
        self.count += value #pyright: ignore
        if save: self.save()

    def clean(self):
        if self.count < 0:
            message = f'Out of max books limit by: {abs(self.count)}' #pyright: ignore
            raise ValidationError(message=message)
        return super().clean()

    name = models.CharField(
        verbose_name='Название',
        max_length=255,
    )
 
    author = models.CharField(
        max_length=256,
        verbose_name='Авторство',
    )

    count = models.PositiveIntegerField(
        verbose_name='В наличии',
        default=0, #pyright: ignore
        help_text='Количество книжних копий в наличии',
    )
    
    certificate_number = models.CharField(
        verbose_name='№ сертификата',
        unique=True,
        max_length=CERTIFICATE_NUMBER_LEN,
        validators=[
            MaxLengthValidator(CERTIFICATE_NUMBER_LEN),
            MinLengthValidator(CERTIFICATE_NUMBER_LEN),
        ],
        help_text=f'Номер состоит из {CERTIFICATE_NUMBER_LEN} символов'
    )

    certification_year = models.SmallIntegerField(
        verbose_name='Год сертификации',
        default=datetime.now().year, #pyright:ignore
        validators=[
            MinValueValidator(1000),
            MaxValueValidator(9999),
        ],
    )

    number_of_printed_pages = models.PositiveIntegerField(
        verbose_name='Количество печатных страниц',
        default=1, #pyright: ignore
        validators=[
            MinValueValidator(1),
            MaxValueValidator(9999),
        ]
    )

    number_of_images = models.PositiveIntegerField(
        verbose_name='Количество рисунков',
        default=0, #pyright: ignore
        validators=[
            MinValueValidator(0),
            MaxValueValidator(9999),
        ],
    )

class BookDebt(models.Model):
    class Meta:
        verbose_name = 'выдачу'
        verbose_name_plural = 'выдача'

    _count_dump = models.PositiveIntegerField(
        editable=False,
        default=0, #pyright: ignore
    )

    user = models.ForeignKey(
        User, 
        verbose_name='Кому',
        on_delete=models.CASCADE,
    )
    book = models.ForeignKey(
        Book, 
        verbose_name='Книга',
        on_delete=models.CASCADE,
    )
    count = models.PositiveIntegerField(
        verbose_name='Экземпляров',
        default=1, #pyright: ignore
        help_text='Количество выдаваемых экземпляров',
        validators=[
            MinValueValidator(1),
        ],
    )
    date_of_issue = models.DateField(
        verbose_name='Дата выдачи',
        auto_now_add=True,
    )
    date_of_deadline = models.DateField(
        verbose_name='Дата возврата',
    )

    def clean(self):
        self._validate_issue_and_deadline()
        # Only decrease count when creating a new debt record
        if self.pk is None:
            self._count_dump = int(self.count) #pyright: ignore
            self.book.decrease_count(value=self.count, save=False) #pyright: ignore
            self.book.clean() #pyright: ignore
            self.book.save() #pyright: ignore
        # rewrite books count 
        self.count = self._count_dump
        super().clean()

    def _validate_issue_and_deadline(self) -> None:
        if self.date_of_deadline <= self.date_of_issue:
            raise ValidationError('Invalid deadline date')


@receiver(post_delete, sender=BookDebt)
def bookdebt_post_delete(sender, instance, **kwargs): #pyright: ignore
    instance.book.increase_count(value=instance.count)
