# Generated by Django 5.1.1 on 2024-09-08 07:11

import django.core.validators
import django.db.models.deletion
from django.conf import settings
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Book',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=255, verbose_name='Название')),
                ('author', models.CharField(max_length=256, verbose_name='Авторство')),
                ('count', models.PositiveIntegerField(default=0, help_text='Количество книжних копий в наличии', verbose_name='В наличии')),
                ('certificate_number', models.CharField(help_text='Номер состоит из 8 символов', max_length=8, unique=True, validators=[django.core.validators.MaxLengthValidator(8), django.core.validators.MinLengthValidator(8)], verbose_name='№ сертификата')),
                ('certification_year', models.SmallIntegerField(default=2024, validators=[django.core.validators.MinValueValidator(1000), django.core.validators.MaxValueValidator(9999)], verbose_name='Год сертификации')),
                ('number_of_printed_pages', models.PositiveIntegerField(default=1, validators=[django.core.validators.MinValueValidator(1), django.core.validators.MaxValueValidator(9999)], verbose_name='Количество печатных страниц')),
                ('number_of_images', models.PositiveIntegerField(default=0, validators=[django.core.validators.MinValueValidator(0), django.core.validators.MaxValueValidator(9999)], verbose_name='Количество рисунков')),
            ],
            options={
                'verbose_name': 'книга',
                'verbose_name_plural': 'книги',
            },
        ),
        migrations.CreateModel(
            name='BookDebt',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('_count_dump', models.PositiveIntegerField(default=0, editable=False)),
                ('count', models.PositiveIntegerField(default=1, help_text='Количество выдаваемых экземпляров', validators=[django.core.validators.MinValueValidator(1)], verbose_name='Экземпляров')),
                ('date_of_issue', models.DateField(auto_now_add=True, verbose_name='Дата выдачи')),
                ('date_of_deadline', models.DateField(verbose_name='Дата возврата')),
                ('book', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='books.book', verbose_name='Книга')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL, verbose_name='Кому')),
            ],
            options={
                'verbose_name': 'выдачу',
                'verbose_name_plural': 'выдача',
            },
        ),
    ]
