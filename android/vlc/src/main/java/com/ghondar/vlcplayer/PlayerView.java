package com.ghondar.vlcplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.xmwsh.videolib.media.IRenderView;
import com.xmwsh.videolib.media.IjkVideoView;
import com.xmwsh.videolib.media.MeasureScanSp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerView extends FrameLayout implements View.OnClickListener {
    private String mUrl;
    private IjkVideoView mVideoView;
    private TableLayout mHudView;

    public PlayerView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        this.initPlayer();
    }

    private void initPlayer() {
        IjkMediaPlayer.loadLibrariesOnce((IjkLibLoader) null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        this.mVideoView = new IjkVideoView(getContext());
        addView(mVideoView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.mHudView = new TableLayout(getContext());
        this.mVideoView.setHudView(this.mHudView);
        int scan = MeasureScanSp.getVideoScan(this.getContext());
    }

    public void onClick(View v) {
    }

    public void stop() {
        if (!this.mVideoView.isBackgroundPlayEnabled()) {
            this.mVideoView.stopPlayback();
            this.mVideoView.release(true);
            this.mVideoView.stopBackgroundPlay();
        } else {
            this.mVideoView.enterBackground();
        }

        IjkMediaPlayer.native_profileEnd();
    }

    public void pause() {
        if (!this.mVideoView.isBackgroundPlayEnabled()) {
            this.mVideoView.pause();
        }
    }

    public void resume() {
        this.mVideoView.resume();
    }

    public void setUrl(String url) {
        this.mUrl = url;
        this.mVideoView.setVideoPath(this.mUrl);
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener listener) {
        this.mVideoView.setOnErrorListener(listener);
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        this.mVideoView.setOnPreparedListener(l);
    }

    public void starPlay() {
        this.mVideoView.start();
    }

    public boolean capturePic() throws NoSuchFieldException, IllegalAccessException {
        Field mRenderViewField = IjkVideoView.class.getDeclaredField("mRenderView");
        if (mRenderViewField != null) {
            mRenderViewField.setAccessible(true);
            IRenderView mIRenderView = (IRenderView) mRenderViewField.get(mVideoView);
            if (mIRenderView != null && mIRenderView.getView() instanceof TextureView) {
                return capturePic((TextureView) mIRenderView.getView());
            } else {
                Log.e("playview", "mRenderView not instance of TextureView");
            }
        }
        return false;
    }

    /**
     * 保存当前视频播放的这一帧到本地
     */
    public boolean capturePic(TextureView mTextureView) {
        Log.e("playview", "capturePic by TextureView");
        boolean isResult = true;
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            String galleryPath = Environment.getExternalStorageDirectory()
                    + File.separator + Environment.DIRECTORY_DCIM
                    + File.separator + "Camera" + File.separator;
            if (mTextureView != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                File file = new File(galleryPath, dateFormat.format(new Date()) + ".png");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap bmp = mTextureView.getBitmap();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Log.e("capture", "capture 7777");
                    ScannerByMedia(getContext(), file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    isResult = false;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Log.e("capture", "capture failure");
            isResult = false;
        }
        return isResult;
    }

    /**
     * 扫描更新图库图片
     *
     * @param context
     * @param path
     */
    private void ScannerByMedia(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
        Log.v("TAG", "media scanner completed");
    }

}
