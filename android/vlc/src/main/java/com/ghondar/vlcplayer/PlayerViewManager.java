package com.ghondar.vlcplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayerViewManager extends SimpleViewManager<PlayerView> {
    public static final String INTENT_ACTIOMN = "com.wsh.full_play_action";
    public static final String FULL_PLAY_ERROR = "VideoFullPlayError";
    public static final String REACT_CLASS = "VideoView";
    public static final String VIDEO_CONTROLL = "VideoControll";
    public static final String VIDEO_ERROR = "VideoError";
    public static final String ACTION = "action";

    public static final String COMMAND_PLAY_NAME = "play";
    public static final String COMMAND_PAUSE_NAME = "pause";
    public static final String COMMAND_FULL_SCREEN_NAME = "full_screen";
    public static final String COMMAND_CAPTURE_NAME = "capture";
    public static final String COMMAND_RECORD_VIDEO_START_NAME = "record_start";
    public static final String COMMAND_RECORD_VIDEO_END_NAME = "record_end";

    public static final int COMMAND_PLAY_ID = 1;
    public static final int COMMAND_PAUSE_ID = 2;
    public static final int COMMAND_FULL_SCREEN_ID = 3;
    public static final int COMMAN_CAPTURE_ID = 4;
    public static final int COMMAND_RECORD_VIDEO_START_ID = 5;
    public static final int COMMAND_RECORD_VIDEO_END_ID = 6;


    private PlayerView mVlcPlayerView;
    private String url;
    private ReactContext mContext;
    private LocalMsgReceiver mLocalMsgReceiver;
    public LifecycleEventListener mActLifeCallback = new LifecycleEventListener() {
        @Override
        public void onHostResume() {
            // 避免在onResume 阶段 黑屏  导致无法继续播放
            if (mVlcPlayerView != null) {
                mVlcPlayerView.starPlay();
            }
            Log.e("PlayerViewManager", "onHostResume ");
        }

        @Override
        public void onHostPause() {
            if (mVlcPlayerView != null) {
                mVlcPlayerView.pause();
            }
            Log.e("PlayerViewManager", "onHostPause ");
        }

        @Override
        public void onHostDestroy() {
            unBindMsgReceiver();
            if (mVlcPlayerView != null) {
                mVlcPlayerView.stop();
            }
            Log.e("PlayerViewManager", "onHostDestroy ");
        }
    };

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected PlayerView createViewInstance(ThemedReactContext reactContext) {
        mContext = reactContext;
        reactContext.addLifecycleEventListener(mActLifeCallback);
        mVlcPlayerView = new PlayerView(reactContext);
        mVlcPlayerView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                sendErrorEvent(mContext, VIDEO_ERROR, "play_error");
                return true;
            }
        });
        mVlcPlayerView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                sendErrorEvent(mContext, VIDEO_ERROR, "load_success");
            }
        });
        bindMsgReceiver();
        return mVlcPlayerView;
    }


    public PlayerView getVlcPlayerView() {
        return mVlcPlayerView;
    }

    /**
     * 设置播放地址的链接
     *
     * @param view
     * @param url
     */
    @ReactProp(name = "url")
    public void setUrl(PlayerView view, String url) {
        this.url = url;
        view.setUrl(url);
        view.starPlay();
        sendErrorEvent(mContext, VIDEO_ERROR, "load_ing");
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(COMMAND_PLAY_NAME, COMMAND_PLAY_ID,
                COMMAND_PAUSE_NAME, COMMAND_PAUSE_ID,
                COMMAND_FULL_SCREEN_NAME, COMMAND_FULL_SCREEN_ID,
                COMMAND_CAPTURE_NAME, COMMAN_CAPTURE_ID,
                COMMAND_RECORD_VIDEO_START_NAME, COMMAND_RECORD_VIDEO_START_ID,
                COMMAND_RECORD_VIDEO_END_NAME, COMMAND_RECORD_VIDEO_END_ID);
    }

    @Override
    public void receiveCommand(PlayerView root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        Log.e("PlayerViewManager", "receiveCommand:" + commandId);
        switch (commandId) {
            case COMMAND_PLAY_ID:
                root.starPlay();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_PAUSE_ID:
                root.pause();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_FULL_SCREEN_ID:
                Player2Activity.go(mContext, url);
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAN_CAPTURE_ID:
                try {
                    boolean result = root.capturePic();
                    sendEvent(mContext, ACTION, result);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendEvent(mContext, ACTION, false);
                }
                break;
            case COMMAND_RECORD_VIDEO_START_ID:

                break;
            case COMMAND_RECORD_VIDEO_END_ID:

                break;
            default:
        }
    }

    private void sendEvent(ReactContext mContext, String key, boolean isSuccess) {
        if (mContext != null) {
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(VIDEO_CONTROLL, createConnectivityEventMap(ACTION, isSuccess));
        }
    }

    private void sendErrorEvent(ReactContext mContext, String type, String key) {
        if (mContext != null) {
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(type, createConnectivityEventMap(key, true));
        }
    }

    private WritableMap createConnectivityEventMap(String key, boolean isSuccess) {
        WritableMap event = new WritableNativeMap();
        event.putBoolean(key, isSuccess);
        return event;
    }

    private void bindMsgReceiver() {
        if (mLocalMsgReceiver == null) {
            mLocalMsgReceiver = new LocalMsgReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ACTIOMN);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalMsgReceiver, filter);
    }

    private void unBindMsgReceiver() {
        if (mLocalMsgReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalMsgReceiver);
        }
    }

    private class LocalMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(INTENT_ACTIOMN, intent.getAction())) {
                String key = intent.getStringExtra("key");
                sendErrorEvent(mContext, VIDEO_ERROR, key);
            }
        }
    }

}
