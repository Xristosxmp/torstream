package com.torstream.app.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.github.se_bastiaan.torrentstream.StreamStatus;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.github.se_bastiaan.torrentstreamserver.TorrentServerListener;
import com.github.se_bastiaan.torrentstreamserver.TorrentStreamNotInitializedException;
import com.github.se_bastiaan.torrentstreamserver.TorrentStreamServer;
import com.torstream.app.R;
import com.torstream.app.databinding.ActivityStreamBinding;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.io.IOException;
import java.util.ArrayList;

public class StreamActivity extends AppCompatActivity implements TorrentServerListener {

    private ActivityStreamBinding binding;
    private TorrentStreamServer server;

    private SurfaceView mVideoSurface = null;
    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private MediaController controller;
    private int mHeight;
    private int mWidth;

    private ArrayList<String> VLC_OPTIONS = new ArrayList<String>() {{
        add("-vvv");
    }};
    static String trackers[] = {
    "udp://tracker.opentrackr.org:1337/announce",
    "udp://open.tracker.cl:1337/announce",
    "udp://open.demonii.com:1337/announce",
    "udp://open.stealth.si:80/announce",
    "udp://exodus.desync.com:6969/announce",
    "udp://tracker.torrent.eu.org:451/announce",
    "udp://tracker1.bt.moack.co.kr:80/announce",
    "udp://tracker-udp.gbitt.info:80/announce",
    "udp://explodie.org:6969/announce",
    "https://tracker.tamersunion.org:443/announce",
    "udp://tracker.tiny-vps.com:6969/announce",
    "udp://tracker.dump.cl:6969/announce",
    "udp://tracker.ccp.ovh:6969/announce",
    "udp://tracker.bittor.pw:1337/announce",
    "udp://run.publictracker.xyz:6969/announce",
    "udp://retracker01-msk-virt.corbina.net:80/announce",
    "udp://public.publictracker.xyz:6969/announce",
    "udp://opentracker.io:6969/announce",
    "udp://open.free-tracker.ga:6969/announce",
    "udp://new-line.net:6969/announce"
    };

    private String var_2746124;
    private View var_7392173013881237b;
    private View var_3949283478217382m;
    private Handler h_1 = new Handler();
    private Handler h_2 = new Handler();
    private Runnable var_7392173013881237b_h = () -> var_7392173013881237b.setVisibility(View.GONE);
    private Runnable var_3949283478217382m_h = () -> var_3949283478217382m.setVisibility(View.GONE);


    @Override
    public void finish() {
        super.finish();
        if (mMediaPlayer != null)  mMediaPlayer.stop();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle var_54216352132136823) {
        super.onCreate(var_54216352132136823);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        binding = ActivityStreamBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());


        binding.goBack.setOnClickListener(v -> finish());
        config_bars();
        String var_2746123 = (String) getIntent().getStringExtra("var_72631623721372137"); // h
        var_2746124 = (String) getIntent().getStringExtra("var_85721631263652312"); // m

        StringBuilder var_base = new StringBuilder ("magnet:?xt=urn:btih:" + var_2746123);
        for(String tr : trackers) var_base.append("&tr=" + tr);
        server = TorrentStreamServer.getInstance();
        server.addListener(this);
        try {
            if(server.isStreaming()) server.stopStream();
            server.startStream(var_base.toString());
        } catch (IOException | TorrentStreamNotInitializedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mHeight = displayMetrics.heightPixels;
        mWidth = displayMetrics.widthPixels;
        var_7392173013881237b = binding.goBack;
        var_3949283478217382m = binding.data;
        config_vlc();

    }

    @Override
    public void onServerReady(String url) {
        binding.spinKit.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);
        controller.setAnchorView(binding.videoSurface);

        if (mLibVLC == null) {
            Log.e("StreamActivity", "LibVLC instance is null");
            return;
        }

        if (url == null) {
            Log.e("StreamActivity", "URL is null");
            return;
        }

        try {
            Media m = new Media(mLibVLC, Uri.parse(url));
            m.setHWDecoderEnabled(true, false);
            if (mMediaPlayer != null) {
                mMediaPlayer.setMedia(m);
                m.release();
                mMediaPlayer.play();
            } else Log.e("StreamActivity", "MediaPlayer instance is null");
            binding.data.setText(var_2746124);
        } catch (Exception e) {
            Log.e("StreamActivity", "Error creating Media: " + e.getMessage(), e);
        }
    }

    @Override
    public void onStreamPrepared(Torrent torrent) {
        Log.d("TORRENT" , "onStreamPrepared");

    }

    @Override
    public void onStreamStarted(Torrent torrent) {
        Log.d("TORRENT" , "onStreamStarted");

    }

    @Override
    public void onStreamError(Torrent torrent, Exception e) {
        Log.d("TORRENT" , "onStreamError");
        Log.d("TORRENT" , e.getMessage());

    }

    @Override
    public void onStreamReady(Torrent torrent) {
        Log.d("TORRENT", "onStreamReady");

    }

    @Override
    public void onStreamProgress(Torrent torrent, StreamStatus status) {
        if(status.bufferProgress < 100) {
            binding.progress.setText(String.valueOf(status.bufferProgress) + "%");
        }
    }

    @Override
    public void onStreamStopped() {

    }

    private IVLCVout vlcVout;
    private void config_vlc(){
        mLibVLC = new LibVLC(this, VLC_OPTIONS);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mVideoSurface = binding.videoSurface;
        vlcVout = mMediaPlayer.getVLCVout();
        vlcVout.setVideoView(mVideoSurface);
        vlcVout.setWindowSize(mWidth,mHeight);
        vlcVout.attachViews();

        controller = new MediaController(this);
        controller.setMediaPlayer(playerInterface);
        for(int i=0; i<controller.getChildCount(); i++){
            View v = controller.getChildAt(i);
            if(v instanceof LinearLayout)
                v.setBackgroundColor(getColor(R.color.transparent));
        }

        binding.videoSurface.setOnClickListener(v -> {
            controller.show(5000);
            var_7392173013881237b.setVisibility(View.VISIBLE);
            var_3949283478217382m.setVisibility(View.VISIBLE);
            h_1.postDelayed(var_7392173013881237b_h, 5000);
            h_2.postDelayed(var_3949283478217382m_h, 5000);
        });
    }

    private MediaController.MediaPlayerControl playerInterface = new MediaController.MediaPlayerControl() {
        public int getBufferPercentage() {
            return 0;
        }

        public int getCurrentPosition() {
            if(vlcVout != null) {
                float pos = mMediaPlayer.getPosition();
                return (int) (pos * getDuration());
            }
            return 0;
        }

        public int getDuration() {
            return  vlcVout != null ? (int)mMediaPlayer.getLength() : 0;
        }

        public boolean isPlaying() {
            return vlcVout != null ? mMediaPlayer.isPlaying() : false;
        }

        public void pause() {
            if(vlcVout != null) mMediaPlayer.pause();
        }

        public void seekTo(int pos) {
            if(vlcVout != null)
            mMediaPlayer.setPosition((float)pos / getDuration());
        }

        public void start() {
            if(vlcVout != null)
            mMediaPlayer.play();
        }

        public boolean canPause() {
            return true;
        }

        public boolean canSeekBackward() {
            return true;
        }

        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return 0;
        }
    };


    private void config_bars(){
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) decorView.setSystemUiVisibility(flags);
                        }
                    });
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        h_1.removeCallbacks(var_7392173013881237b_h);
        h_2.removeCallbacks(var_3949283478217382m_h);

        if(vlcVout != null){
            vlcVout.detachViews();
            vlcVout = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mLibVLC != null) {
            mLibVLC.release();
            mLibVLC = null;
        }
        if(server.isStreaming()) server.stopStream();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLibVLC == null) mLibVLC = new LibVLC(this, VLC_OPTIONS);
        if (mMediaPlayer == null)  mMediaPlayer = new MediaPlayer(mLibVLC);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.getVLCVout().detachViews();
        }
    }


}
