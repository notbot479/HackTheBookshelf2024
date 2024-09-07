from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework import status

from .models import UserNotification


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
