from django.utils.html import format_html
from django.contrib import admin
from django.urls import reverse

from urllib.parse import urlencode

from .models import UserNotification


@admin.register(UserNotification)
class UserNotificationAdmin(admin.ModelAdmin):
    list_display = ('user', 'firebase_push_button')
    ordering = ('-created_at',)

    def firebase_push_button(self, obj):
        params = {
            'firebase_push_hash': obj.firebase_push_hash,
            'title': 'Test title',
            'description': 'It Was Me, Dio!',
        }
        title = 'Send test push'
        url = reverse('send_notification')+'?'+urlencode(params)
        return format_html('<a href="{}">{}</a>', url, title)
