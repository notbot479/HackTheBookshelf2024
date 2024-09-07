# Generated by Django 5.1.1 on 2024-09-07 18:59

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('books', '0005_alter_bookdebt_date_of_deadline_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='book',
            name='count',
            field=models.PositiveIntegerField(default=0, help_text='Количество книжних копий в наличии', verbose_name='В наличии'),
        ),
        migrations.AlterField(
            model_name='bookdebt',
            name='count',
            field=models.PositiveIntegerField(default=1, help_text='Количество выдаваемых экземпляров', verbose_name='Экземпляров'),
        ),
    ]
