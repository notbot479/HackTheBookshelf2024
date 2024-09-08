from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated, IsAdminUser
from rest_framework.response import Response
from rest_framework import status

from firebase_admin.exceptions import InvalidArgumentError
from firebase_admin import messaging
import firebase_admin
import logging

from .models import UserNotification
import config


# init firebase app
credentials = firebase_admin.credentials.Certificate(
    cert=config.FIREBASE_CERTIFICATE_PATH,
)
app = firebase_admin.initialize_app(credentials)


def firebase_send_message(token:str, title:str, description:str) -> bool:
    try:
        notification = messaging.Notification(title, description)
        message = messaging.Message(token=token, notification=notification)
        response = messaging.send(message)
        logging.debug(response)
        return True
    except messaging.UnregisteredError:
        logging.warning('Update firebase token required')
        return False
    except InvalidArgumentError:
        return False


@api_view(['GET'])
@permission_classes([IsAdminUser])
def send_notification(request):
    firebase_push_hash = request.GET.get('firebase_push_hash')
    title = request.GET.get('title','')
    description = request.GET.get('description','')
    if not(firebase_push_hash):
        data = {'error': 'firebase_push_hash is required'}
        return Response(data, status=status.HTTP_200_OK)
    ok = firebase_send_message(
        token = firebase_push_hash,
        title=title,
        description=description,
    )
    return Response({'status': ok}, status=status.HTTP_200_OK)


@api_view(['POST'])
@permission_classes([IsAuthenticated])
def sub_to_notifications(request):
    firebase_push_hash = request.data.get('firebase_push_hash')
    if not(firebase_push_hash):
        data = {'error': 'firebase_push_hash is required'}
        return Response(data, status=status.HTTP_200_OK)
    # create sub to notification or update token
    n = UserNotification.objects.filter(user=request.user).first() #pyright: ignore
    if not(n):
        n = UserNotification.objects.create( #pyright: ignore
            user=request.user,
            firebase_push_hash=firebase_push_hash,
        )
        message = "Subscribed to notifications"
    else:
        n.firebase_push_hash = firebase_push_hash
        n.save()
        message = "Update notification token"
    data = {'message': message}
    return Response(data, status=status.HTTP_200_OK)

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def ignore_notifications(request):
    try:
        n = UserNotification.objects.get(user=request.user) #pyright: ignore
        n.delete()
    except:
        pass
    return Response({'status': 'Ignore new notifications'}, status=status.HTTP_200_OK)
