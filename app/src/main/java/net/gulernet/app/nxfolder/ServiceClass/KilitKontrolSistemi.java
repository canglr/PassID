package net.gulernet.app.nxfolder.ServiceClass;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.UiClass.MainActivity;
import net.gulernet.app.nxfolder.UiClass.kontrolislemleri;
import net.gulernet.app.nxfolder.UiClass.pingiris;
import net.gulernet.app.nxfolder.UiClass.yeniguncelleme;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by necip on 20.02.2018.
 */

public class KilitKontrolSistemi extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();


       final Handler handler = new Handler();
        Timer timer;


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(GlobalKod.ekrankilitlimi(getApplicationContext()))
                        {
                            /*GlobalKod.pingiris = false;
                            Intent intent = new Intent(getApplicationContext(), pingiris.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);*/
                            GlobalKod.guvenlicikis(getApplicationContext());
                            System.exit(0);

                        }

                    }
                });
            }
        };

        timer = new Timer();

        timer.schedule(timerTask,0,2000);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
