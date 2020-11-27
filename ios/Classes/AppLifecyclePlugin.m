#import "AppLifecyclePlugin.h"
#if __has_include(<app_lifecycle/app_lifecycle-Swift.h>)
#import <app_lifecycle/app_lifecycle-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "app_lifecycle-Swift.h"
#endif

@implementation AppLifecyclePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAppLifecyclePlugin registerWithRegistrar:registrar];
}
@end
