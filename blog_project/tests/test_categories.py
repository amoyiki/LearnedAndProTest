from blog.models import Category

def add(name):
    c = Category
    c.name = "测试分类"
    c.save()