package net.gulernet.app.nxfolder.UiClass;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;
import com.squareup.picasso.Picasso;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.ServiceClass.SikistirmaKontrol;
import net.gulernet.app.nxfolder.ServiceClass.ThumbnailKontrol;

public class pingiris extends AppCompatActivity implements FingerPrintAuthCallback {
    Button pinbtn;
    EditText pinsifresi;
    TextView kayippin;
    TextView kayippin2;
    LinearLayout parmakizilayout;
    LinearLayout AnaSifreLayout;
    private FingerPrintAuthHelper mFingerPrintAuthHelper;
    GlobalKod global = new GlobalKod();
    String Parmakizidurumu;
    String Parmakizivepin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pingiris);
        pinsifresi = (EditText) findViewById(R.id.pin);
        pinsifresi.requestFocus(); // pin şifresi bölümünde imleci getirir.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); //klavyeyi gösterir.
        pinbtn = (Button) findViewById(R.id.pinbtn);
        kayippin = (TextView) findViewById(R.id.kayippin);
        kayippin2 = (TextView) findViewById(R.id.kayippin2);
        parmakizilayout = (LinearLayout) findViewById(R.id.parmakizilayout);
        AnaSifreLayout = (LinearLayout) findViewById(R.id.AnaSifreLayout);




        if(GlobalKod.checkWriteExternalPermission(getApplicationContext())) {

            Intent msgIntent = new Intent(getApplicationContext(), ThumbnailKontrol.class);
            getApplicationContext().startService(msgIntent);


            Intent sikistirmakontrol = new Intent(getApplicationContext(), SikistirmaKontrol.class);
            getApplicationContext().startService(sikistirmakontrol);
        }

        pinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinsifre = GlobalKod.readSharedPreference(getApplicationContext(),"pinsifre","null");
                String sahtepinsifre = GlobalKod.readSharedPreference(getApplicationContext(),"sahtepinsifre","null");
                String c3 = pinsifresi.getText().toString();
                if(c3.equals(pinsifre))
                {
                    GlobalKod.pingiris = true;
                    Intent main = new Intent(pingiris.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }else if(c3.equals(sahtepinsifre))
                {
                    Intent sahtepin = new Intent(pingiris.this, net.gulernet.app.nxfolder.UiClass.sahtepin.class);
                    startActivity(sahtepin);
                    finish();
                }else{
                    pinsifresi.setError(getResources().getString(R.string.The_pin_is_faulty));
                }

            }
        });

        kayippin.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
           GlobalKod.pinyonlendirme = true;
           GlobalKod.pinadres = kontrolislemleri.class;
           Intent kontrol = new Intent(pingiris.this, net.gulernet.app.nxfolder.UiClass.kullanicikontrol.class);
           startActivity(kontrol);
           finish();

             }
         });

        kayippin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalKod.pinyonlendirme = true;
                GlobalKod.pinadres = kontrolislemleri.class;
                Intent kontrol = new Intent(pingiris.this, net.gulernet.app.nxfolder.UiClass.kullanicikontrol.class);
                startActivity(kontrol);
                finish();

            }
        });

        Parmakizidurumu = GlobalKod.readSharedPreference(getApplicationContext(),"parmakizidurumu","false");
        Parmakizivepin = GlobalKod.readSharedPreference(getApplicationContext(),"parmakizivepin","false");


        if(Parmakizivepin.equals("true"))
        {
            pinbtn.setVisibility(View.GONE);
            parmakizilayout.setVisibility(View.VISIBLE);

        }else if(Parmakizidurumu.equals("true"))
        {
            parmakizilayout.setVisibility(View.VISIBLE);
            AnaSifreLayout.setVisibility(View.GONE);
            kayippin2.setVisibility(View.VISIBLE);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
        }


        if (Build.VERSION.SDK_INT >= 23) {
            if(Parmakizidurumu.equals("true") || Parmakizivepin.equals("true")) {
                mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
            }
        }
        //GlobalKod.İzinİste(getApplicationContext());  izin isteme kütüphanesi gerekli görülmediği için kullanımına son verildi!

        if (Build.VERSION.SDK_INT >= 23) {
//kullanıcıdan izin isteme metodu !
//Check whether your app has access to the READ permission//

            if (checkPermission()) {

//If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//


            } else {

//If your app doesn’t have permission to access external storage, then call requestPermission//

                requestPermission();
            }
        }


    }


    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(pingiris.this, Manifest.permission.READ_EXTERNAL_STORAGE);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (Build.VERSION.SDK_INT >= 23) {

            switch (requestCode) {
                case 5:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent i = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        GlobalKod global = new GlobalKod();
                        global.BildirimOlustur(getApplicationContext(), getResources().getString(R.string.Application_failed_to_initialize), getResources().getString(R.string.The_application_failed_to_start));
                        System.exit(0);

                    }
                    break;
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //start finger print authentication
        if (Build.VERSION.SDK_INT >= 23) {
            if(Parmakizidurumu.equals("true") || Parmakizivepin.equals("true")) {
                mFingerPrintAuthHelper.startAuth();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23) {
            if(Parmakizidurumu.equals("true") || Parmakizivepin.equals("true")) {
                mFingerPrintAuthHelper.stopAuth();
            }
        }
    }

    @Override
    public void onNoFingerPrintHardwareFound() {

    }

    @Override
    public void onNoFingerPrintRegistered() {

    }

    @Override
    public void onBelowMarshmallow() {

    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        //https://github.com/multidots/android-fingerprint-authentication
        if(Parmakizidurumu.equals("true"))
        {
            GlobalKod.pingiris = true;
            Intent main = new Intent(pingiris.this, MainActivity.class);
            startActivity(main);
            finish();
        }else if(Parmakizivepin.equals("true"))
        {
            String pinsifre = GlobalKod.readSharedPreference(getApplicationContext(),"pinsifre","null");
            String sahtepinsifre = GlobalKod.readSharedPreference(getApplicationContext(),"sahtepinsifre","null");
            String c3 = pinsifresi.getText().toString();
            if(c3.equals(pinsifre))
            {
                GlobalKod.pingiris = true;
                Intent main = new Intent(pingiris.this, MainActivity.class);
                startActivity(main);
                finish();
            }else if(c3.equals(sahtepinsifre))
            {
                Intent sahtepin = new Intent(pingiris.this, net.gulernet.app.nxfolder.UiClass.sahtepin.class);
                startActivity(sahtepin);
                finish();
            }else{
                pinsifresi.setError(getResources().getString(R.string.The_pin_is_faulty));
                mFingerPrintAuthHelper.startAuth();
            }
        }else{

        }



    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Your_fingerprint_was_not_recognized));
                mFingerPrintAuthHelper.startAuth();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //global.ToastOlustur(getApplicationContext(),"Parmak izi tanıma işlemi başlatılamadı");
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:

                break;
        }
    }




}
