onload=function(){
  chrome.tabs.getSelected(function(tab){
      //QRCode(元素id,相关配置文件)
      var qrcode = new QRCode("qrcode", {
              text: tab.url,
              width: 160,
              height: 160,
              colorDark : '#000000',
              colorLight : '#ffffff',
              // QRCode的容错级别
              correctLevel : QRCode.CorrectLevel.H
            });
    if (tab.favIconUrl) {//tab有图标的情况下动态赋值
      var img = document.getElementsByTagName("img")[1].src = tab.favIconUrl;
    }
    console.log(img);
  });
}


