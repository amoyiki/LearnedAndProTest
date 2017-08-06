from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.common.action_chains import ActionChains
import time
import os

# cookies 字段
cookie_dictionary = {
    'newstatisticUUID': '',
    'hiijack': '',
    'e1': '',
    'e2': '',
    'ywkey': '',
    'ywguid': '',
    'al': '',
    'cmfuToken': '',
    'qduid': '',
}

dcap = dict(DesiredCapabilities.PHANTOMJS)

# User Agent
dcap["phantomjs.page.settings.userAgent"] = (
    "Mozilla/5.0 (iPod; U; CPU iPhone OS 2_1 like Mac OS X; ja-jp) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5F137 Safari/525.20"
)

browser = webdriver.PhantomJS(executable_path='phantomjs.exe',desired_capabilities=dcap)
browser.get('http://m.qidian.com/')
cap_dict = browser.desired_capabilities

newwindow2='window.open("http://m.qidian.com/book/{}");'.format('')   # 投票的BOOK ID
browser.delete_all_cookies()

for item in cookie_dictionary.keys():
    browser.add_cookie({'domain': '.qidian.com',
                        'name': item,
                        'value': cookie_dictionary[item],
                        'path': '/',
                        })
# 打开并切换到新窗口
browser.execute_script(newwindow2)
browser.switch_to.window(browser.window_handles[1])

time.sleep(11)  # 适当的等待时间
# browser.save_screenshot('0.png')  # 页面截图

# xpath获得投票按钮位置，点击
f = browser.find_element_by_xpath("//ul[@id='payTicketsX']/li[2]/a")
ActionChains(browser).double_click(f).perform()
time.sleep(3)
f = browser.find_element_by_xpath("//input[@id='tkR2-all']")
ActionChains(browser).double_click(f).perform()
f = browser.find_element_by_xpath("//div[@class='popup-pay-submit']/input[@id='paySm-2']")
ActionChains(browser).click(f).perform()

time.sleep(10)
# browser.save_screenshot('1.png')

browser.quit()
