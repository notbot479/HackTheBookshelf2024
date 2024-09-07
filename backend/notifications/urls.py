from django.urls import path
from . import views


urlpatterns = [
    path('sub/', views.sub_to_notifications, name='sub_to_notifications'),
    path('ignore/', views.ignore_notifications, name='ignore_notifications'),
    path('send/', views.send_notification, name='send_notification'),
]
