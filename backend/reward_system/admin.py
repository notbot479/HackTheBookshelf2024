from django.contrib import admin

from .models import UserRewardCouter, UserAchievement


@admin.register(UserRewardCouter)
class UserRewardCounterAdmin(admin.ModelAdmin):
    list_display = ('user', 'event_counter', 'books_recovered')  
    search_fields = ('user__username',)  
    list_filter = ('event_counter', 'books_recovered')  


@admin.register(UserAchievement)
class UserAchievementAdmin(admin.ModelAdmin):
    list_display = ('user', 'name', 'created_at')
    search_fields = ('user__username', 'name')
    list_filter = ('created_at',)
    ordering = ('created_at',)
