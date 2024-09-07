from django.contrib import admin
from .models import Event


@admin.register(Event)
class EventAdmin(admin.ModelAdmin):
    list_display = (
        'name', 
        'start_time', 
        'end_time', 
        'is_active_today', 
        'is_active_now', 
        'is_upcoming', 
        'is_register_available',
        'is_hidden',
    )
    ordering = ('start_time',)
    search_fields = ('name', 'description')
    list_filter=('start_time', 'end_time')


    def is_register_available(self, obj) -> bool:
            return obj.is_register_available()
    is_register_available.short_description = 'Регистрация' #pyright: ignore
    is_register_available.boolean = True #pyright: ignore

    def is_active_now(self, obj) -> bool:
        return obj.is_active_now()
    is_active_now.short_description = 'Сейчас' #pyright: ignore
    is_active_now.boolean = True #pyright: ignore

    def is_active_today(self, obj) -> bool:
        return obj.is_active_today()
    is_active_today.short_description = 'Сегодня' #pyright: ignore
    is_active_today.boolean = True #pyright: ignore

    def is_upcoming(self, obj) -> bool:
        return obj.is_upcoming()
    is_upcoming.short_description = 'Ближайшие' #pyright: ignore
    is_upcoming.boolean = True #pyright: ignore

    def is_hidden(self, obj) -> bool:
        return obj.hidden
    is_hidden.short_description = 'Скрытое' #pyright: ignore
    is_hidden.boolean = True #pyright: ignore
