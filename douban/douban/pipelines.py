# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
from pymongo import MongoClient
from scrapy.conf import settings
from scrapy import log

class DoubanPipeline(object):
    def process_item(self, item, spider):
        return item

class MongoDBPipeline(object):
    def __init__(self):
        connection = MongoClient(
            host=settings['MONGODB_SERVER'],
            port=settings['MONGODB_PORT']
        )
        db = connection[settings['MONGODB_DB']]
        self.collection = db[settings['MONGODB_COLLECTION']]

    def process_item(self, item, spider):
        self.collection.insert(dict(item))
        log.msg("Book  added to MongoDB database!",
                level=log.DEBUG, spider=spider)
        return item