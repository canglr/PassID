package net.gulernet.app.nxfolder.UiClass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.gulernet.app.nxfolder.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class sahtepin extends AppCompatActivity {
    TextView dogrulamakodu;
    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sahtepin);
        dogrulamakodu = (TextView) findViewById(R.id.dogrulamakodu);


        timer = new Timer();
        timer.schedule(new firstTask(), 0, 10000);


    }


    class firstTask extends TimerTask {
        @Override
        public void run() {
            sahtepin.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Random rnd = new Random();
                    String [] koddizisi=new String[8];
                    String kod="";
                   for (int i=0;i<=7;i++) {
                       koddizisi[i] = Integer.toString(rnd.nextInt(10));
                       kod += koddizisi[i];
                       if(i==3)
                       {kod +=" ";}
                   }

                    dogrulamakodu.setText(kod);
                }
            });
        }
    };


}
