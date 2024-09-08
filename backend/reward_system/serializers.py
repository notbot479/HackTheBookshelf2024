from rest_framework import serializers
from .models import UserAchievement


class UserAchievementSerializer(serializers.ModelSerializer):
    created_at_timestamp = serializers.SerializerMethodField()

    class Meta:
        model = UserAchievement
        fields = ['id', 'name', 'created_at_timestamp']

    def get_created_at_timestamp(self, obj) -> int:
        return int(obj.created_at.timestamp())

 
