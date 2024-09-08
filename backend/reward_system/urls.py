# urls.py
from django.urls import path
from .views import UserAchievementListCreateView

urlpatterns = [
    path('my/', UserAchievementListCreateView.as_view(), name='user-achievement-list-create'),
]

