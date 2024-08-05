package com.torstream.app.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    static String trackers[] = {
            "http://tracker.opentrackr.org:1337/announce",
            "udp://tracker.auctor.tv:6969/announce",
            "udp://opentracker.i2p.rocks:6969/announce",
            "https://opentracker.i2p.rocks:443/announce",
            "udp://open.demonii.com:1337/announce",
            "udp://open.stealth.si:80/announce",
            "udp://tracker.torrent.eu.org:451/announce",
            "udp://tracker.moeking.me:6969/announce",
            "udp://exodus.desync.com:6969/announce",
            "udp://tracker1.bt.moack.co.kr:80/announce",
            "udp://tracker.tiny-vps.com:6969/announce",
            "udp://tracker.skyts.net:6969/announce",
            "udp://open.tracker.ink:6969/announce",
            "udp://movies.zsw.ca:6969/announce",
            "udp://explodie.org:6969/announce",
            "https://tracker.tamersunion.org:443/announce",
            "udp://tracker2.dler.org:80/announce",
            "udp://tracker.therarbg.com:6969/announce",
            "udp://tracker.theoks.net:6969/announce",
            "udp://tracker.qu.ax:6969/announce",
            "http://tracker.opentrackr.org:1337/announce",
            "udp://open.tracker.cl:1337/announce",
            "udp://open.demonii.com:1337/announce",
            "udp://open.stealth.si:80/announce",
            "udp://tracker.torrent.eu.org:451/announce",
            "udp://exodus.desync.com:6969/announce",
            "udp://tracker-udp.gbitt.info:80/announce",
            "udp://explodie.org:6969/announce",
            "http://tracker1.bt.moack.co.kr:80/announce",
            "udp://tracker.tiny-vps.com:6969/announce",
            "udp://tracker.dump.cl:6969/announce",
            "udp://tracker.dler.org:6969/announce",
            "udp://tracker.bittor.pw:1337/announce",
            "udp://opentracker.io:6969/announce",
            "udp://leet-tracker.moe:1337/announce",
            "udp://isk.richardsw.club:6969/announce",
            "udp://bt.ktrackers.com:6666/announce",
            "https://tracker.tamersunion.org:443/announce",
            "https://tracker.renfei.net:443/announce",
            "http://tracker.renfei.net:8080/announce"
    };



    @Override
    protected void onCreate(@Nullable Bundle var_54216352132136823) {
        super.onCreate(var_54216352132136823);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        binding = ActivityStreamBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        config_bars();
        String var_2746123 = (String) getIntent().getStringExtra("var_72631623721372137"); // h
        String var_2746124 = (String) getIntent().getStringExtra("var_85721631263652312"); // m


        StringBuilder var_base = new StringBuilder ("magnet:?xt=urn:btih:" + var_2746123);
        for(String tr : trackers) var_base.append("&tr=" + tr);
        server = TorrentStreamServer.getInstance();
        server.addListener(this);
        try { server.startStream(var_base.toString());}
        catch (IOException e) {e.printStackTrace();   throw new RuntimeException(e);}
        catch (TorrentStreamNotInitializedException e) {e.printStackTrace();   throw new RuntimeException(e); }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mHeight = displayMetrics.heightPixels;
        mWidth = displayMetrics.widthPixels;

        config_vlc();

    }

    @Override
    public void onServerReady(String url) {
        Media m = new Media(mLibVLC, Uri.parse(url));
        mMediaPlayer.setMedia(m);
        m.release();
        m.setHWDecoderEnabled(true,false);
        m.addOption(":network-caching=100");
        m.addOption(":clock-jitter=0");
        m.addOption(":clock-synchro=0");
        mMediaPlayer.setAspectRatio("16:9");
        mMediaPlayer.play();

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
            Log.d("TORRENT", "Progress: " + status.bufferProgress);
            Log.d("TORRENT" , "Speed: " + status.downloadSpeed);
        }
    }

    @Override
    public void onStreamStopped() {

    }


    private void config_vlc(){
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        mLibVLC = new LibVLC(this, args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mVideoSurface = binding.videoSurface;
        mVideoSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        final IVLCVout vlcVout = mMediaPlayer.getVLCVout();
        vlcVout.setVideoView(mVideoSurface);
        vlcVout.setWindowSize(mWidth,mHeight);
        vlcVout.attachViews();

        controller = new MediaController(this);
        controller.setMediaPlayer(playerInterface);
        controller.setAnchorView(binding.videoSurface);
        binding.videoSurface.setOnClickListener(v -> controller.show(5000));
    }

    private MediaController.MediaPlayerControl playerInterface = new MediaController.MediaPlayerControl() {
        public int getBufferPercentage() {
            return 0;
        }

        public int getCurrentPosition() {
            float pos = mMediaPlayer.getPosition();
            return (int)(pos * getDuration());
        }

        public int getDuration() {
            return (int)mMediaPlayer.getLength();
        }

        public boolean isPlaying() {
            return mMediaPlayer.isPlaying();
        }

        public void pause() {
            mMediaPlayer.pause();
        }

        public void seekTo(int pos) {
            mMediaPlayer.setPosition((float)pos / getDuration());
        }

        public void start() {
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
        mMediaPlayer.release();
        mLibVLC.release();
        server.stopStream();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
        mMediaPlayer.getVLCVout().detachViews();
    }


}
