package net.gulernet.app.nxfolder.UiClass;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;


import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

import java.io.IOException;



public class video_play extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    public static String Video_Url;
    public static String Video_Dosyaadi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_video_play);


        mediaController = new MediaController(video_play.this);
        videoView = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse(Video_Url);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.start();
        GlobalKod.Reklamyukle(getApplicationContext());

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        GlobalKod.Reklamgoster();
    }


}
