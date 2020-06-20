package com.example.weibodemo.WeiboLogin;

import io.flutter.embedding.android.FlutterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WeiboFlutterActivity extends FlutterActivity {

    public static class WeiboCachedEngineIntentBuilder extends FlutterActivity.CachedEngineIntentBuilder {

        protected WeiboCachedEngineIntentBuilder(Class<? extends FlutterActivity> activityClass, String engineId) {
            super(activityClass, engineId);
        }
    }

    public static WeiboCachedEngineIntentBuilder engine() {

        return new WeiboCachedEngineIntentBuilder(WeiboFlutterActivity.class, "weibo_engine");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeiboDismissEvent event) {

        Log.v("weibo","eventBus receive WeiboDismissEvent");
        Intent intent = new Intent();
        intent.putExtra("access_token",event.access_token);
        setResult(12138, intent);
        finish();
    };
}
