"""routing URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from app import views

urlpatterns = [
    path('getchatts/', views.getchatts, name='getchatts'),
    path('admin/', admin.site.urls),
    path('postchatt/', views.postchatt, name='postchatt'),
    path('getimages/', views.getimages, name='getimages'),
    path('postimages/', views.postimages, name='postimages'),
    path('getmaps/', views.getmaps, name='getmaps'),
    path('postmaps/', views.postmaps, name='postmaps'),
    path('getaudio/', views.getaudio, name='getaudio'),
    path('postaudio/', views.postaudio, name='postaudio'),
    path('postauth/', views.postauth, name='postauth'),
    path('adduser/', views.adduser, name='adduser'),
]
