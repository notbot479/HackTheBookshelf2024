from django.urls import path

from .views import *


urlpatterns = [
    path('', EventListView.as_view(), name='event-list'),
    path('upcoming/', UpcomingEventsView.as_view(), name='upcoming-events'),
    path('active-today/', ActiveTodayEventsView.as_view(), name='active-today-events'),
    path('active-now/', ActiveNowEventsView.as_view(), name='active-now-events'),
    path('register-available/', RegisterAvailableEventsView.as_view(), name='register-available-events'),
]
