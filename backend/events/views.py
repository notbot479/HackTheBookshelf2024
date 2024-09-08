from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from django.contrib.auth.models import User
from rest_framework.views import APIView
from rest_framework import generics
from rest_framework import status
from django.utils import timezone

from .serializers import EventSerializer
from .models import Event

from reward_system.models import UserRewardCouter


def increase_event_counter(user: User, value:int=1) -> None:
    rc,_ = UserRewardCouter.objects.get_or_create(user=user) #pyright: ignore
    rc.event_counter += value
    rc.save()

class UserEvents(generics.ListAPIView):
    serializer_class = EventSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        events = Event.objects.filter(hidden=False, attendees=user) #pyright: ignore
        return events

class RegisterToEvent(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request, event_id:int):
        user = request.user
        try:
            event = Event.objects.get(id=event_id) #pyright: ignore
        except:
            data = {'error': f'Failed get event by id={event_id}'}
            return Response(data, status=status.HTTP_200_OK)
        register = event.is_register_available()
        if not(register):
            data = {'error': f'Registration for current event unavailable'}
            return Response(data, status=status.HTTP_200_OK)
        already_registered = event.attendees.filter(id=user.id).exists() #pyright: ignore
        if already_registered:
            data = {'error': f'You already registered for current event'}
            return Response(data, status=status.HTTP_200_OK)
        increase_event_counter(user=user)
        event.attendees.add(user)
        return Response({'status': True}, status=status.HTTP_200_OK)

    def delete(self, request, event_id:int):
        try:
            event = Event.objects.get(id=event_id) #pyright: ignore
        except:
            data = {'error': f'Failed get event by id={event_id}'}
            return Response(data, status=status.HTTP_200_OK)
        event.attendees.remove(request.user)
        return Response({'status': True}, status=status.HTTP_200_OK)


class EventListView(generics.ListAPIView):
    queryset = Event.objects.filter( #pyright: ignore
        hidden=False,
    )
    serializer_class = EventSerializer

class RegisterAvailableEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.localtime()
        events = Event.objects.filter( #pyright: ignore
            is_registration_open=True,
            start_time__gte=now,
            hidden=False,
        )
        return events

class UpcomingEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.localtime()
        events = Event.objects.filter( #pyright: ignore
            start_time__gt=now,
            hidden=False,
        )
        return events

class ActiveNowEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.localtime()
        events = Event.objects.filter( #pyright: ignore
            start_time__lte=now, 
            end_time__gte=now,
            hidden=False,
        )
        return events

class ActiveTodayEventsView(generics.ListAPIView):
    serializer_class = EventSerializer

    def get_queryset(self):
        now = timezone.localtime()
        events = Event.objects.filter( #pyright: ignore
            start_time__date__lte=now.date(), 
            end_time__date__gte=now.date(),
            hidden=False,
        )
        return events
