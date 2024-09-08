from rest_framework_simplejwt.tokens import RefreshToken, AccessToken
from rest_framework.response import Response
from rest_framework import generics, status
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from typing import Any

from .serializers import GetTokenSerializer


def get_user_from_token(token: Any) -> User | None:
    try:
        token = AccessToken(token)
        user_id = token.payload['user_id']
        user = User.objects.get(id=user_id)
        return user
    except:
        return None


class GetJwtTokenView(generics.GenericAPIView):
    serializer_class = GetTokenSerializer

    @staticmethod
    def _get_token_for_user(user: User) -> str:
        refresh = RefreshToken.for_user(user)
        token = str(refresh.access_token) #pyright: ignore
        return token

    def _create_user(self, data:dict) -> User | None:
        serializer = self.get_serializer(data=data)
        ok = serializer.is_valid()
        if not(ok): return None
        user = serializer.save()
        return user

    def post(self, request, *args, **kwargs): #pyright: ignore
        username = request.data.get('username')
        password = request.data.get('password')
        # identify user or create new account
        new_account_created = False
        user = authenticate(request, username=username, password=password)
        if not(user):
            new_account_created = True
            user = self._create_user(data=request.data)
        # if failed create user via current data
        if not(user):
            data = {'error': 'Invalid credentials, check your data'}
            return Response(data=data, status=status.HTTP_200_OK)
        # generate token for current user
        token = self._get_token_for_user(user=user)
        data = {
            'new_account_created': new_account_created,
            'token': token,
        }
        return Response(data, status=status.HTTP_201_CREATED)
