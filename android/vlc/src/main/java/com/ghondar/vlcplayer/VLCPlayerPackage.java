package com.ghondar.vlcplayer;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VLCPlayerPackage implements ReactPackage {

    private PlayerViewManager mPlayerViewManager;

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new VLCPlayer(reactContext));
        return modules;
    }

    // Deprecated RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        mPlayerViewManager = new PlayerViewManager();
        return Arrays.<ViewManager>asList(
                mPlayerViewManager
        );
    }

    public PlayerViewManager getPlayerViewManager() {
        return mPlayerViewManager;
    }
}
