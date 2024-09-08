from django.urls import path

from .views import GetJwtTokenView


urlpatterns = [
    path('get_token/', GetJwtTokenView.as_view(), name='get_jwt_token'),
]
