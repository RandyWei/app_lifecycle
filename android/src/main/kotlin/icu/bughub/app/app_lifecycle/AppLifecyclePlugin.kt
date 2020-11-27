package icu.bughub.app.app_lifecycle

import androidx.annotation.NonNull
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.chinahrt.app.pharmacist.QueuingEventSink
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.HiddenLifecycleReference
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** AppLifecyclePlugin */
class AppLifecyclePlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private lateinit var observer: LifeCycleObserver
    private lateinit var lifecycle: Lifecycle
    private lateinit var eventChannel: EventChannel
    private lateinit var pluginBinding: FlutterPlugin.FlutterPluginBinding

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "app_lifecycle")
        channel.setMethodCallHandler(this)
        pluginBinding = flutterPluginBinding
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        setup(binding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        tearDown()
    }

    private fun setup(activityBinding: ActivityPluginBinding) {
        eventChannel = EventChannel(pluginBinding.binaryMessenger, "icu.bughub.plugin.app_lifecycle/event")
        val queuingEventSink = QueuingEventSink()
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                queuingEventSink.setDelegate(events)
            }

            override fun onCancel(arguments: Any?) {
                queuingEventSink.setDelegate(null)
            }

        })
        observer = LifeCycleObserver(queuingEventSink)
        lifecycle = (activityBinding.lifecycle as HiddenLifecycleReference).lifecycle
        lifecycle.addObserver(observer)
    }

    private fun tearDown() {
        lifecycle.removeObserver(observer)
    }
}

class LifeCycleObserver(var queuingEventSink: QueuingEventSink) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onCreate"
        queuingEventSink.success(map)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onStart"
        queuingEventSink.success(map)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onResume"
        queuingEventSink.success(map)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onPause"
        queuingEventSink.success(map)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onStop"
        queuingEventSink.success(map)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        val map = HashMap<String, Any>()
        map["event"] = "onDestroy"
        queuingEventSink.success(map)
    }
}
