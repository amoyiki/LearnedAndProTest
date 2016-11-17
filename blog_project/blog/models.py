from django.db import models
from django.contrib import admin
from collections import defaultdict
import datetime
# Create your models here.
class ArticleManage(models.Manager):
    """
    继承Manager，新增归类archive方法
    """
    def archive(self):
        date_list = Article.objects.datetimes('created_time','month',order='DESC')
        data_dict = defaultdict(list)
        for d in date_list:
            data_dict[d.year].append(d.month)
        return sorted(data_dict.items(), reverse=True)

class Article(models.Model):
    STATUS_CHOICES = (
        ('d', 'Draft'),
        ('p', 'Published'),
    )
    objects = ArticleManage()

    title = models.CharField('标题', max_length=70,null=False)
    body = models.TextField('正文',null=False)
    created_time = models.DateTimeField('创建时间', auto_now_add=True)
    last_modified_time = models.DateTimeField('修改时间', auto_now=True)
    status = models.CharField('文章状态', max_length=1, choices=STATUS_CHOICES)
    abstract = models.CharField('摘要', max_length=54, blank=True, null=True,
                                help_text="可选，若为空则摘抄正文前54个字符")
    views = models.PositiveIntegerField('浏览量', default=0)
    likes = models.PositiveIntegerField('点赞数', default=0)
    topped = models.BooleanField('置顶', default=False)
    category = models.ForeignKey('Category', verbose_name='分类', null=True,
                                 on_delete=models.SET_NULL)
    tags = models.ManyToManyField('Tag', verbose_name='标签集合', blank=True)


    def __str__(self):
        return self.title

    class Meta:
        verbose_name = '文章'
        verbose_name_plural = verbose_name
        ordering = ['-last_modified_time']

admin.site.register(Article)

class Category(models.Model):
    name = models.CharField('类名', max_length=20)
    create_time = models.DateTimeField('创建时间', auto_now_add=True)
    last_modified_time = models.DateTimeField('修改时间', auto_now=True)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = '分类'
        verbose_name_plural = verbose_name

admin.site.register(Category)

class Tag(models.Model):
    name = models.CharField('标签名', max_length=20)
    created_time = models.DateTimeField('创建时间', auto_now_add=True)
    last_modified_time = models.DateTimeField('修改时间', auto_now=True)

    def __str__(self):
        return self.name

    class Meta:
        verbose_name = '标签'
        verbose_name_plural = verbose_name

admin.site.register(Tag)