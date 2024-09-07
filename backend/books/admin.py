from django.contrib import admin

from .models import Book, BookDebt


@admin.register(Book)
class BookAdmin(admin.ModelAdmin):
    list_display = ('name', 'author', 'count', 'certificate_number', 'certification_year', 'number_of_printed_pages', 'number_of_images')
    list_filter = ('certification_year', )
    search_fields = ('name', 'certificate_number', 'count', 'author',)
    ordering = ('name',)


@admin.register(BookDebt)
class BookDebtAdmin(admin.ModelAdmin):
    list_display = ('user', 'book', 'count','date_of_issue', 'date_of_deadline')
    list_filter = ('date_of_issue', 'date_of_deadline')
    search_fields = ('user', 'book', 'date_of_issue', 'date_of_deadline')
    ordering = ('date_of_deadline',)
