from django.contrib import admin
from django.urls import path, include


urlpatterns = [
    path('admin/', admin.site.urls),
    path('jwt/', include('jwt_auth.urls')),
    path('events/', include('events.urls')),
    path('notifications/', include('notifications.urls')),
]
