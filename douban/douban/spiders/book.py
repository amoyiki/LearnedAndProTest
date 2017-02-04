# -*- coding: utf-8 -*-
import scrapy
from scrapy.selector import Selector
from douban.items import BookItem

class BookSpider(scrapy.Spider):
    name = "book"
    allowed_domains = ["book.douban.com"]
    start_urls = ['https://book.douban.com/tag/%E6%97%A5%E6%9C%AC%E6%96%87%E5%AD%A6?start=0']

    def parse(self, response):
        sel = Selector(response)
        book_list = sel.css('#subject_list > ul > li')
        for book in book_list:
            item = BookItem()
            try:
                item['book_name'] = book.xpath('div[@class="info"]/h2/a/text()').extract()[0].strip()
                item['book_star'] = book.xpath("div[@class='info']/div[2]/span[@class='rating_nums']/text()").extract()[0].strip()
                item['book_pl'] = book.xpath("div[@class='info']/div[2]/span[@class='pl']/text()").extract()[0].strip()
                pub = book.xpath('div[@class="info"]/div[@class="pub"]/text()').extract()[0].strip().split('/')
                item['book_price'] = pub.pop()
                item['book_date'] = pub.pop()
                item['book_publish'] = pub.pop()
                item['book_author'] = '/'.join(pub)
                yield item
            except:
                pass
        nextPage = sel.xpath('//div[@id="subject_list"]/div[@class="paginator"]/span[@class="next"]/a/@href').extract()[0].strip()
        if nextPage:
            next_url = 'https://book.douban.com'+nextPage
            yield scrapy.http.Request(next_url,callback=self.parse)