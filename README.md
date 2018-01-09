# Android蓝牙工具

## 功能

* 封装经典蓝牙、低功耗蓝牙BLE
* 检索：不过滤检索、过滤名字检索、过滤Mac地址检索
* UUID：不过滤、过滤
* 通讯：读取、写入蓝牙信息

## 使用

* 申请权限
如果需要动态申请权限，可以参考[permission][permission]
```
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.BLUETOOTH_PRIVILEGED"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

## 书籍

>- [BLE和经典蓝牙Android编程说明][BLE和经典蓝牙Android编程说明]


[permission]:https://github.com/VeiZhang/Permission
[BLE和经典蓝牙Android编程说明]:https://github.com/VeiZhang/BluetoothKit/blob/master/book/BLE%E5%92%8C%E7%BB%8F%E5%85%B8%E8%93%9D%E7%89%99Android%E7%BC%96%E7%A8%8B%E8%AF%B4%E6%98%8E.pdf