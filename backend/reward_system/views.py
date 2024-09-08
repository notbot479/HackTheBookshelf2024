from rest_framework.permissions import IsAuthenticated
from rest_framework import generics

from .models import UserAchievement
from .serializers import UserAchievementSerializer


class UserAchievementListCreateView(generics.ListCreateAPIView):
    serializer_class = UserAchievementSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return UserAchievement.objects.filter(user=user) #pyright: ignore

