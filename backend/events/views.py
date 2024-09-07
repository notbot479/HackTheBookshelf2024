from rest_framework import generics
from django.utils import timezone

from .serializers import EventSerializer
from .models import Event


class EventListView(generics.ListAPIView):
    queryset = Event.objects.filter( #pyright: ignore
        hidden=False,
    )
    serializer_class = EventSerializer

class RegisterAvailableEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.now()
        events = Event.objects.filter( #pyright: ignore
            is_registration_open=True,
            start_time__gte=now,
            hidden=False,
        )
        return events

class UpcomingEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.now()
        events = Event.objects.filter( #pyright: ignore
            start_time__gt=now,
            hidden=False,
        )
        return events

class ActiveNowEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.now()
        events = Event.objects.filter( #pyright: ignore
            start_time__lte=now, 
            end_time__gte=now,
            hidden=False,
        )
        return events

class ActiveTodayEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.now()
        events = Event.objects.filter( #pyright: ignore
            start_time__date__lte=now.date(), 
            end_time__date__gte=now.date(),
            hidden=False,
        )
        return events
