package com.example.weibodemo;

import android.app.Application;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterMain;

public class MainAplication extends Application {

    FlutterEngine weiboEngine;
    EventChannel weiboEventChannel;
    EventChannel.EventSink weiboEventSink;

    MethodChannel weiboMethodChannel;
    @Override
    public void onCreate() {
        super.onCreate();

        //注册eventBus
        EventBus.getDefault().register(this);

        getWeiboEngine();
    }

    //如果没有则创建
    public FlutterEngine getWeiboEngine() {
        weiboEngine = FlutterEngineCache.getInstance().get("weibo_engine");
        if (weiboEngine == null) {

            weiboEngine = new FlutterEngine(this);
            //new FlutterEngine 必须要让engine知道执行哪个方法,android默认不知道;
            DartExecutor.DartEntrypoint dartEntrypoint = new DartExecutor.DartEntrypoint(FlutterMain.findAppBundlePath(),"main");
            weiboEngine.getDartExecutor().executeDartEntrypoint(dartEntrypoint);

            FlutterEngineCache.getInstance().put("weibo_engine",weiboEngine);

            weiboEventChannel = new EventChannel(weiboEngine.getDartExecutor(),"weibo_event");
            weiboEventChannel.setStreamHandler(new EventChannel.StreamHandler() {
                @Override
                public void onListen(Object arguments, EventChannel.EventSink events) {

                    if (arguments instanceof Map) {
                        Map<String,String> arg = (Map<String,String>)arguments;

                        String access_token = arg.get("access_token");

                        if (!access_token.isEmpty()) {

                            Log.v("weibo","Native get access_token with first event channel:" + access_token);
                        } else {
                            Log.v("weibo","Native have not init access_token yet.");
                        }
                    }
                    weiboEventSink = events;
                }

                @Override
                public void onCancel(Object arguments) {

                    Log.v("weibo",arguments.toString());
                }
            });

            weiboMethodChannel = new MethodChannel(weiboEngine.getDartExecutor(),"weibo_method");
            weiboMethodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
                @Override
                public void onMethodCall(MethodCall call, MethodChannel.Result result) {

                    if (call.method.equals("access_token")) {

                        String access_token = call.argument("access_token");

                        Log.v("weibo","Native get access_token with mothod channel:" + access_token);

                        result.success(null);
                    }
                }
            });
        }
        return weiboEngine;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeiboLoginEvent event) {

        Log.v("weibo","eventBus receive WeiboLoginEvent");
    };

    @Override
    public void onTerminate() {
        super.onTerminate();

        EventBus.getDefault().unregister(this);
    }
}
