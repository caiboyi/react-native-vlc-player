package com.ghondar.vlcplayer;

import android.app.Application;
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
import com.xmwsh.ivmsvideolib.utils.VideoMgr;
import com.xmwsh.ivmsvideolib.widget.VideoPlayerView;

import java.util.Map;

import javax.annotation.Nullable;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayerViewManager extends SimpleViewManager<VideoPlayerView> {
    public static final String INTENT_ACTIOMN = "com.wsh.full_play_action";
    public static final String FULL_PLAY_ERROR = "VideoFullPlayError";
    public static final String REACT_CLASS = "VideoView";
    public static final String VIDEO_CONTROLL = "VideoControll";
    public static final String VIDEO_ERROR = "VideoError";
    public static final String ACTION = "action";

    //播放
    public static final String COMMAND_PLAY_NAME = "play";
    //停止播放
    public static final String COMMAND_STOP_PLAY_NAME = "stop";

    public static final String COMMAND_PAUSE_NAME = "pause";
    //全屏
    public static final String COMMAND_FULL_SCREEN_NAME = "full_screen";
    //退出全屏
    public static final String COMMAND_EXIT_SCREEN_NAME = "exit_full_screen";
    public static final String COMMAND_CAPTURE_NAME = "capture";
    public static final String COMMAND_RECORD_VIDEO_START_NAME = "record_start";
    public static final String COMMAND_RECORD_VIDEO_END_NAME = "record_end";
    //高清
    public static final String COMMAND_HIGHT_STREM_NAME = "hight_stream";
    //标清
    public static final String COMMAND_STANDE_STREM_NAME ="stande_stream";
    //流畅
    public static final String COMMAND_FLUENCY_STREM_NAME ="fluency_stream";
    //向左
    public static final String COMMAND_TO_LEFT_NAME = "left";
    //向右
    public static final String COMMAND_TO_RIGHT_NAME = "right";
    //向上
    public static final String COMMAND_TO_UP_NAME = "up";
    //向下
    public static final String COMMAND_TO_DOWN_NAME = "down";
    //内焦距
    public static final String COMMAND_TO_IN_FOCAL_LENGTH_NAME = "in_focal_length";
    //外焦距
    public static final String COMMAND_TO_OUT_FOCAL_LENGTH_NAME = "out_focal_length";
    //近焦点
    public static final String COMMAND_TO_NEAR_FOCUS_NAME = "near_focus";
    //远焦点
    public static final String COMMAND_TO_FAR_FOCUS_NAME = "far_focus";
    //自动
    public static final String COMMAND_AUTO_NAME = "auto";

    public static final int COMMAND_PLAY_ID = 1;
    public static final int COMMAND_PAUSE_ID = 2;
    public static final int COMMAND_FULL_SCREEN_ID = 3;
    public static final int COMMAN_CAPTURE_ID = 4;
    public static final int COMMAND_RECORD_VIDEO_START_ID = 5;
    public static final int COMMAND_RECORD_VIDEO_END_ID = 6;
    public static final int COMMAND_EXIT_FULL_SCREEN_ID = 7;
    public static final int COMMAND_HIGHT_STREM_ID = 8;
    public static final int COMMAND_STANDE_STREM_ID = 9;
    public static final int COMMAND_FLUENCY_STREM_ID = 10;
    public static final int COMMAND_TO_LEFT_ID = 11;
    public static final int COMMAND_TO_RIGHT_ID = 12;
    public static final int COMMAND_TO_UP_ID = 13;
    public static final int COMMAND_TO_DOWN_ID = 14;
    public static final int COMMAND_TO_IN_FCAL_LENGTH_ID = 15;
    public static final int COMMAND_TO_OUT_FCAL_LENGTH_ID = 16;
    public static final int COMMAND_TO_NEAR_FOCUS_ID = 17;
    public static final int COMMAND_TO_FAR_FOCUS_ID = 18;
    public static final int COMMAND_AUTO_ID = 19;
    public static final int COMMAND_STOP_PLAY_ID = 20;




    private VideoPlayerView mVlcPlayerView;
    private String url;
    private ReactContext mContext;
    private LocalMsgReceiver mLocalMsgReceiver;
    public LifecycleEventListener mActLifeCallback = new LifecycleEventListener() {
        @Override
        public void onHostResume() {
            // 避免在onResume 阶段 黑屏  导致无法继续播放
            if (mVlcPlayerView != null) {
                mVlcPlayerView.startPlay();
            }
            Log.e("PlayerViewManager", "onHostResume ");
        }

        @Override
        public void onHostPause() {
            if (mVlcPlayerView != null) {
                mVlcPlayerView.stopPlay();
            }
            Log.e("PlayerViewManager", "onHostPause ");
        }

        @Override
        public void onHostDestroy() {
            unBindMsgReceiver();
            if (mVlcPlayerView != null) {
                //mVlcPlayerView.stop();
                mVlcPlayerView.release();
            }
            Log.e("PlayerViewManager", "onHostDestroy ");
        }
    };

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected VideoPlayerView createViewInstance(ThemedReactContext reactContext) {
        mContext = reactContext;
        VideoMgr.initIVMSSDK(reactContext.getCurrentActivity().getApplication());
        reactContext.addLifecycleEventListener(mActLifeCallback);
        mVlcPlayerView = new VideoPlayerView(reactContext);
//        mVlcPlayerView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                sendErrorEvent(mContext, VIDEO_ERROR, "play_error");
//                return true;
//            }
//        });
//        mVlcPlayerView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IMediaPlayer iMediaPlayer) {
//                sendErrorEvent(mContext, VIDEO_ERROR, "load_success");
//            }
//        });
        bindMsgReceiver();
        return mVlcPlayerView;
    }


    public VideoPlayerView getVlcPlayerView() {
        return mVlcPlayerView;
    }

    /**
     * 设置播放地址的链接
     *
     * @param view
     * @param url
     */
    @ReactProp(name = "url")
    public void setUrl(VideoPlayerView view, String url) {
        this.url = url;
        Log.e("test","setUrl url : "+url);
        view.setLivePlayUrl(url);
        //view.setUrl(url);
        //view.starPlay();
        sendErrorEvent(mContext, VIDEO_ERROR, "load_ing");
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer>  map = MapBuilder.of();
        map.put(COMMAND_PLAY_NAME,COMMAND_PLAY_ID);
        map.put(COMMAND_PAUSE_NAME,COMMAND_PAUSE_ID);
        map.put(COMMAND_FULL_SCREEN_NAME,COMMAND_FULL_SCREEN_ID);
        map.put(COMMAND_CAPTURE_NAME,COMMAN_CAPTURE_ID);
        map.put(COMMAND_EXIT_SCREEN_NAME,COMMAND_EXIT_FULL_SCREEN_ID);
        map.put(COMMAND_HIGHT_STREM_NAME,COMMAND_HIGHT_STREM_ID);
        map.put(COMMAND_STANDE_STREM_NAME,COMMAND_STANDE_STREM_ID);
        map.put(COMMAND_FLUENCY_STREM_NAME,COMMAND_FLUENCY_STREM_ID);
        map.put(COMMAND_TO_LEFT_NAME,COMMAND_TO_LEFT_ID);
        map.put(COMMAND_TO_RIGHT_NAME,COMMAND_TO_RIGHT_ID);
        map.put(COMMAND_TO_UP_NAME,COMMAND_TO_UP_ID);
        map.put(COMMAND_TO_DOWN_NAME,COMMAND_TO_DOWN_ID);
        map.put(COMMAND_TO_IN_FOCAL_LENGTH_NAME,COMMAND_TO_IN_FCAL_LENGTH_ID);
        map.put(COMMAND_TO_OUT_FOCAL_LENGTH_NAME,COMMAND_TO_OUT_FCAL_LENGTH_ID);
        map.put(COMMAND_TO_NEAR_FOCUS_NAME,COMMAND_TO_NEAR_FOCUS_ID);
        map.put(COMMAND_TO_FAR_FOCUS_NAME,COMMAND_TO_FAR_FOCUS_ID);
        map.put(COMMAND_AUTO_NAME,COMMAND_AUTO_ID);
        map.put(COMMAND_RECORD_VIDEO_START_NAME,COMMAND_RECORD_VIDEO_START_ID);
        map.put(COMMAND_RECORD_VIDEO_END_NAME,COMMAND_RECORD_VIDEO_END_ID);
        map.put(COMMAND_STOP_PLAY_NAME,COMMAND_STOP_PLAY_ID);
        return map;
    }

    @Override
    public void receiveCommand(VideoPlayerView root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        Log.e("PlayerViewManager", "receiveCommand:" + commandId);
        switch (commandId) {
            case COMMAND_PLAY_ID://开始播放
                root.startPlay();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_STOP_PLAY_ID://停止播放
                root.stopPlay();
                sendEvent(mContext, ACTION, true);
                break;
//            case COMMAND_PAUSE_ID:
//                //root.pause();
//                //sendEvent(mContext, ACTION, true);
//                break;
            case COMMAND_FULL_SCREEN_ID://全屏
                root.enterFullScreen();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_EXIT_FULL_SCREEN_ID://退出全屏
                root.exitFullScreen();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAN_CAPTURE_ID://截屏
                root.capture();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_RECORD_VIDEO_START_ID://开始录频
                root.startLiveRecord();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_RECORD_VIDEO_END_ID://结束录频
                root.stopLiveRecord();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_LEFT_ID ://向左
                root.ptzCommandUp();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_RIGHT_ID ://向右
                root.ptzCommandRight();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_UP_ID ://向上
                root.ptzCommandUp();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_DOWN_ID ://向下
                root.ptzCommandDown();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_IN_FCAL_LENGTH_ID ://内焦距
                root.addFocalLength();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_OUT_FCAL_LENGTH_ID ://外焦距
                root.subtractFocalLength();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_NEAR_FOCUS_ID ://近焦点
                root.addFocus();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_TO_FAR_FOCUS_ID ://远焦点
                root.subtractFocus();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_AUTO_ID://自动
                root.ptzCommandAuto();
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_HIGHT_STREM_ID://高清
                root.setStreamType(1);
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_STANDE_STREM_ID://标清
                root.setStreamType(2);
                sendEvent(mContext, ACTION, true);
                break;
            case COMMAND_FLUENCY_STREM_ID://流畅
                root.setStreamType(3);
                sendEvent(mContext, ACTION, true);
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
