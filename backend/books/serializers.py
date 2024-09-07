from rest_framework import serializers
from .models import BookDebt, Book


class BookSerializer(serializers.ModelSerializer):
    class Meta:
        model = Book
        exclude=['count',]

class DebtSerializer(serializers.ModelSerializer):
    book = BookSerializer()

    class Meta:
        model = BookDebt
        fields=['book','count','date_of_issue','date_of_deadline']
