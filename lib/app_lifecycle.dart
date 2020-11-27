import 'dart:async';

import 'package:flutter/services.dart';

class AppLifecycle {
  static const MethodChannel _channel = const MethodChannel('app_lifecycle');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void addObserver(
      {Function onCreate,
      Function onStart,
      Function onResume,
      Function onPause,
      Function onStop,
      Function onDestroy}) {
    EventChannel("icu.bughub.plugin.app_lifecycle/event")
        .receiveBroadcastStream()
        .listen((value) {
      if (value['event'] == "onCreate") {
        onCreate?.call();
      } else if (value['event'] == "onStart") {
        onStart?.call();
      } else if (value['event'] == "onResume") {
        onResume?.call();
      } else if (value['event'] == "onPause") {
        onPause?.call();
      } else if (value['onStop'] == "onStop") {
        onCreate?.call();
      } else if (value['event'] == "onDestroy") {
        onDestroy?.call();
      }
    });
  }
}
