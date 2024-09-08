from django.contrib.auth.models import User
from django.db import models


class UserNotification(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    firebase_push_hash = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self) -> str:
        return self.user.username #pyright: ignore
