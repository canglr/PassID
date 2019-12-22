package net.gulernet.app.nxfolder.UiClass;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class image_play extends AppCompatActivity {

    public static String İmage_url;
//https://android-arsenal.com/details/1/1509
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image_play);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.uri(İmage_url));
        GlobalKod.Reklamyukle(getApplicationContext());
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        GlobalKod.Reklamgoster();
    }


}
