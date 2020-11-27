# app_lifecycle

安卓MainActivity生命周期监听

## 安装

```
//pub方式
暂无

//导入方式
dependencies:
  app_lifecycle:
    git:
      url: https://github.com/RandyWei/app_lifecycle.git
```

## 示例
```dart
AppLifecycle.addObserver(onCreate: () {
      print("onCreate");
    }, onStart: () {
      print("onStart");
    }, onDestroy: () {
      print("onDestroy");
    }, onPause: () {
      print("onPause");
    }, onResume: () {
      print("onResume");
    }, onStop: () {
      print("onStop");
    });
```