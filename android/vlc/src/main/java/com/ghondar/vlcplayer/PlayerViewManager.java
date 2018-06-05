package com.ghondar.vlcplayer;

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

public class PlayerViewManager extends SimpleViewManager<VlcPlayerView> {
    public static final String REACT_CLASS = "VideoView";
	
	public static final String VIDEO_CONTROLL = "VideoControll";
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


    private VlcPlayerView mVlcPlayerView;
    private String url;
	private ReactContext mContext;
    public LifecycleEventListener mActLifeCallback = new LifecycleEventListener() {
        @Override
        public void onHostResume() {
            // 避免在onResume 阶段 黑屏  导致无法继续播放
            if (mVlcPlayerView != null) {
//                mVlcPlayerView.releasePlayer();
                mVlcPlayerView.resumePlay();
            }
            Log.e("PlayerViewManager", "onHostResume ");
        }

        @Override
        public void onHostPause() {
            if (mVlcPlayerView != null) {
                mVlcPlayerView.pausePlay();
            }
            Log.e("PlayerViewManager", "onHostPause ");
        }

        @Override
        public void onHostDestroy() {
            if (mVlcPlayerView != null) {
                mVlcPlayerView.releasePlayer();
            }
            Log.e("PlayerViewManager", "onHostDestroy ");
        }
    };

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected VlcPlayerView createViewInstance(ThemedReactContext reactContext) {
		mContext = reactContext;
        reactContext.addLifecycleEventListener(mActLifeCallback);
        mVlcPlayerView = new VlcPlayerView(reactContext);
        mVlcPlayerView.toggleFullscreen(true);
        return mVlcPlayerView;
    }

    /**
     * 设置播放地址的链接
     *
     * @param view
     * @param url
     */
    @ReactProp(name = "url")
    public void setUrl(VlcPlayerView view, String url) {
        this.url = url;
        view.releasePlayer();
        view.playMovie(url);
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
    public void receiveCommand(VlcPlayerView root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        Log.e("PlayerViewManager", "receiveCommand:" + commandId);
        switch (commandId) {
            case COMMAND_PLAY_ID:
                root.resumePlay();
                sendEvent(mContext, COMMAND_PLAY_NAME, true);
                break;
            case COMMAND_PAUSE_ID:
                root.pausePlay();
                sendEvent(mContext, COMMAND_PAUSE_NAME, true);
                break;
            case COMMAND_FULL_SCREEN_ID:
                root.fullScreen();
                sendEvent(mContext, COMMAND_FULL_SCREEN_NAME, true);
                break;
            case COMMAN_CAPTURE_ID:
                root.capturePic();
                sendEvent(mContext, COMMAND_CAPTURE_NAME, true);
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
                    .emit(VIDEO_CONTROLL, createConnectivityEventMap(key, isSuccess));
        }
    }

    private WritableMap createConnectivityEventMap(String key, boolean isSuccess) {
        WritableMap event = new WritableNativeMap();
        event.putBoolean(ACTION, isSuccess);
        return event;
    }
}
