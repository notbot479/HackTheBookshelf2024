from django.contrib import admin

from .models import UserRewardCouter


@admin.register(UserRewardCouter)
class UserRewardCounterAdmin(admin.ModelAdmin):
    list_display = ('user', 'event_counter', 'books_recovered')  
    search_fields = ('user__username',)  
    list_filter = ('event_counter', 'books_recovered')  
