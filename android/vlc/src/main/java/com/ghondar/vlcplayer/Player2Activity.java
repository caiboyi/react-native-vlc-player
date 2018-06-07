package com.ghondar.vlcplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.view.WindowManager;

import com.xmwsh.videolib.media.IRenderView;
import com.xmwsh.videolib.media.IjkVideoView;

import java.lang.reflect.Field;

public class Player2Activity extends Activity {

    public static void go(Context context, String url) {
        Intent intent = new Intent(context, Player2Activity.class);
        intent.putExtra(LOCATION, url);
        context.startActivity(intent);
    }

    public final static String LOCATION = "srcVideo";
    private PlayerView playerView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Log.e("Player", "this is Player2Activity");
        playerView = new PlayerView(this);
        setContentView(playerView);
        Intent intent = getIntent();
        url = intent.getExtras().getString(LOCATION);
        playerView.setUrl(url);
        playerView.starPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playerView != null) {
            playerView.starPlay();
        }
        Log.e("Player2Activity", " onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerView != null) {
            playerView.pause();
        }
        Log.e("Player2Activity", "pause");
    }

    @Override
    protected void onDestroy() {
        if (playerView != null) {
            playerView.stop();
        }
        super.onDestroy();
        Log.e("Player2Activity", "destroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        Process.killProcess(Process.myPid());
    }
}
