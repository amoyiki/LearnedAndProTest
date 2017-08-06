from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.common.action_chains import ActionChains
import time
from datetime import datetime

cookie_dictionary = {'GED_PLAYLIST_ACTIVITY': '',
    'Hm_lvt_299cfc89fdba155674e083d478408f29': '',
    'PHPSESSID': '',
    'ZQ_GUID':'',
    'ZQ_GUID_C':'',
    'share_cookie': '',
    'gid': '1876469010',
    'cookie_ip': '',
    'taskTipCookie108518583': '{}'.format(datetime.now().date().strftime('%Y-%m-%d')),
    'tj_uid': ''
}
dcap = dict(DesiredCapabilities.PHANTOMJS)

# User Agent
dcap["phantomjs.page.settings.userAgent"] = (
'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36'
)

follow_list = ['']  # 需成为粉丝后的直播间才能签到

url=''  # 随意某个直播间链接
browser = webdriver.PhantomJS(executable_path='phantomjs.exe',desired_capabilities=dcap)
browser.get(url)
newwindow2='window.open("https://www.zhanqi.tv/{}");'
browser.delete_all_cookies()

for item in cookie_dictionary.keys():
    browser.add_cookie({'domain': '.zhanqi.tv',
                        'name': item,
                        'value': cookie_dictionary[item],
                        'path': '/',
                        })
for up in follow_list:
    browser.execute_script(newwindow2.format(up))
    browser.switch_to.window(browser.window_handles[1])
    time.sleep(10)
    f = browser.find_element_by_xpath("//a[@id='js-fans-sign-btn']")
    name = browser.find_element_by_xpath("//p[@class ='name dv']")
    print('{} --------------------{}--------------------'.format(name.text, f.text))
    ActionChains(browser).click(f).perform()
    browser.close()
    browser.switch_to.window(browser.window_handles[0])
browser.quit()
