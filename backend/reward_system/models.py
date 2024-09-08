from django.db.models.signals import pre_save
from django.dispatch import receiver
from django.contrib.auth.models import User
from django.db import models
from typing import Iterator

from notifications.views import firebase_send_message
from notifications.models import UserNotification


def create_new_achievement(user: User, name:str) -> None:
    UserAchievement.objects.get_or_create(user=user,name=name) #pyright: ignore

def get_firebase_token_by_user(user: User) -> str | None:
    token = UserNotification.objects.get(user=user) #pyright: ignore
    if not(token): return None
    return token

def counter_reward(old, new, attribute: str, *, k: int) -> Iterator:
    k1 = (getattr(old, attribute) // k) + 1
    k2 = (getattr(new, attribute) // k) + 1
    if k1 == k2: return
    for i in range(k1, k2):
        stage = i * k
        if not stage: continue
        yield stage


class UserAchievement(models.Model):
    class Meta:
        verbose_name = 'Достижение'
        verbose_name_plural = 'Достижения'

    user = models.ForeignKey(User, on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)

class UserRewardCouter(models.Model):
    class Meta:
        verbose_name = 'Счетчик'
        verbose_name_plural = 'Счетчики'
 
    user = models.OneToOneField(
        User, 
        on_delete=models.CASCADE,
        verbose_name='Пользователь'
    )
    event_counter = models.PositiveIntegerField(
        verbose_name='Количество регистраций на мероприятия', #pyright: ignore
        default=0, #pyright: ignore
    )
    books_recovered = models.PositiveIntegerField(
        verbose_name='Количество возвращенных книг', #pyright: ignore
        default=0, #pyright: ignore
    )


def event_counter_reward(old, new, *, k:int=5) -> None:
    for stage in counter_reward(old=old,new=new,attribute='event_counter',k=k):
        name = f'The bar has been reached at {stage} visited events.'
        create_new_achievement(user=new.user, name=name)
        token = get_firebase_token_by_user(user=new.user)
        if not(token): continue
        title = 'Congratulations! New achievement received.'
        firebase_send_message(token=token,title=title,description=name)

def books_counter_reward(old, new, *, k:int=10) -> None:
    for stage in counter_reward(old=old,new=new,attribute='books_recovered',k=k):
        name = f'The bar has been reached at {stage} recovered books.'
        create_new_achievement(user=new.user, name=name)
        token = get_firebase_token_by_user(user=new.user)
        if not(token): continue
        title = 'Congratulations! New achievement received.'
        firebase_send_message(token=token,title=title,description=name)


@receiver(pre_save, sender=UserRewardCouter)
def compare_old_new_data(sender, instance, **kwargs): #pyright: ignore
    if not(instance.pk): return
    old_instance = sender.objects.get(pk=instance.pk)
    event_counter_reward(old=old_instance, new=instance)
    books_counter_reward(old=old_instance, new=instance)
