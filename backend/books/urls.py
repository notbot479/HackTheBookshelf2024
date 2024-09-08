from django.urls import path
from .views import *


urlpatterns = [
    path('debt/my/', user_debts, name='user-debts'),
]
