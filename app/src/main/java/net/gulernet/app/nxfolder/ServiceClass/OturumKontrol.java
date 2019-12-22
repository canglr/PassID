package net.gulernet.app.nxfolder.ServiceClass;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gulernet.app.nxfolder.BgClass.Cryptotxt;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.UiClass.MainActivity;
import net.gulernet.app.nxfolder.UiClass.kontrolislemleri;
import net.gulernet.app.nxfolder.UiClass.pingiris;
import net.gulernet.app.nxfolder.UiClass.yeniguncelleme;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import github.nisrulz.easydeviceinfo.base.EasyIdMod;

import static com.android.volley.VolleyLog.TAG;
import static net.gulernet.app.nxfolder.BgClass.GlobalKod.SunucuAdresi;

/**
 * Created by necip on 20.02.2018.
 */

public class OturumKontrol extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        String oturum = GlobalKod.readSharedPreference(getApplicationContext(),"oturum","");
        if(!oturum.equals("")) {

            final Handler handler = new Handler();
            Timer timer;


            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                BgOturumKontrol task = new BgOturumKontrol(getApplicationContext());
                                task.execute();

                            }catch (Exception e)
                            {

                            }

                        }
                    });
                }
            };

            timer = new Timer();

            timer.schedule(timerTask, 0, 30000);
        }
        return START_NOT_STICKY;
    }


    public static class BgOturumKontrol extends AsyncTask<Void, Integer, Void> {

        Context c;

        public BgOturumKontrol(Context c) {
            this.c = c;
        }

        public void OturumuKapat(){
            GlobalKod global = new GlobalKod();
            global.BildirimOlustur(c,c.getResources().getString(R.string.Security),c.getResources().getString(R.string.The_session_expired_sign_in_again));
            GlobalKod.OturumuKapat(c);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(c);
            queue.getCache().clear();
            String oturum = Cryptotxt.encrypt(GlobalKod.readSharedPreference(c,"oturum",""));

            final String url = SunucuAdresi+"/kullanicilar/oturum?zn10="+ Uri.encode(oturum);

// prepare the Request

            StringRequest jsonForGetRequest = new StringRequest(
                    Request.Method.GET,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject obj = new JSONObject(response);
                                String name = Cryptotxt.decrypt(obj.getString("oturumkontrol"));

                                if(name.equals("false"))
                                {
                                    OturumuKapat();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof NetworkError) {
                                Log.e(TAG, error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e(TAG, error.getMessage());
                            }


                        }
                    }
            );

// add it to the RequestQueue
            jsonForGetRequest.setShouldCache(false);
            queue.add(jsonForGetRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];

        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);

        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
