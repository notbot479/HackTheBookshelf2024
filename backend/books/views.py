from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .serializers import DebtSerializer
from .models import BookDebt


@api_view(['GET'])
@permission_classes([IsAuthenticated])
def user_debts(request):
    user = request.user
    debts = BookDebt.objects.filter(user=user) #pyright: ignore
    serializer = DebtSerializer(debts, many=True)
    return Response(serializer.data)
