from rest_framework import serializers
from django.utils import timezone

from .models import Event


class EventSerializer(serializers.ModelSerializer):
    start_timestamp = serializers.SerializerMethodField()
    end_timestamp = serializers.SerializerMethodField()
    is_register_available = serializers.SerializerMethodField()

    class Meta:
        model = Event
        exclude = [
            'hidden',
            'start_time',
            'end_time',
            'is_registration_open',
            'attendees',
        ]

    def get_start_timestamp(self, obj) -> int:
        return int(obj.start_time.timestamp())

    def get_end_timestamp(self, obj) -> int:
        return int(obj.end_time.timestamp())

    def get_is_register_available(self,obj) -> bool:
        now = timezone.localtime()
        return obj.start_time > now and bool(obj.is_registration_open)
