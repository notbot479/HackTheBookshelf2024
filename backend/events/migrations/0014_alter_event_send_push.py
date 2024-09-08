# Generated by Django 5.1.1 on 2024-09-08 00:50

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0013_alter_event_send_push'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='send_push',
            field=models.BooleanField(default=False, help_text='Для скрытых мероприятий push уведомления отключены', verbose_name='Отправить push'),
        ),
    ]
