package com.ghondar.vlcplayer;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;

public class ScreenModule extends ReactContextBaseJavaModule {

    private Context mContext;
    private View mActionBar;
    public ScreenModule(ReactApplicationContext reactContext, View actionBar) {
        super(reactContext);
        this.mContext = reactContext;
        this.mActionBar = actionBar;
    }

    @Override
    public String getName() {
        return "ScreenModule";
    }

    /**
     * 获取状态栏和导航栏高度
     * @param isShow
     * @param promise
     */
    @ReactMethod
    public void getActionBarHeight(boolean isShow,Promise promise) {
        int height = DensityUtil.getStatusHeight(mContext);
        if (mActionBar != null) {
            if (isShow) {
                mActionBar.setVisibility(View.VISIBLE);
            }else {
                mActionBar.setVisibility(View.GONE);
            }
            height += mActionBar.getHeight();
        }
        try {
            WritableMap map = Arguments.createMap();
            map.putDouble("height", DensityUtil.px2dip(mContext,height));
            promise.resolve(map);
        }catch (IllegalViewOperationException e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void testAc(boolean msg,Promise promise) {
        Toast.makeText(getReactApplicationContext(),""+msg,Toast.LENGTH_LONG).show();
        try {
            WritableMap map = Arguments.createMap();
            map.putDouble("height",10);
            promise.resolve(map);
        }catch (IllegalViewOperationException e) {
            promise.reject(e);
        }
    }


}
