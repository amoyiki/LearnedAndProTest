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
    console.log(qrcode);
  });
}


