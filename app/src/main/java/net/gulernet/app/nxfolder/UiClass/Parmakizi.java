package net.gulernet.app.nxfolder.UiClass;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class Parmakizi extends AppCompatActivity implements FingerPrintAuthCallback {
    Switch parmakizi;
    Switch parmakizivepin;
    String parmakizidurumux;
    String parmakizivepinx;

    GlobalKod global = new GlobalKod();
    private FingerPrintAuthHelper mFingerPrintAuthHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parmakizi);
        parmakizi = (Switch) findViewById(R.id.parmakizi);
        parmakizivepin = (Switch) findViewById(R.id.parmakizivepin);
        getSupportActionBar().setTitle(getResources().getString(R.string.finger_print));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 23) {
            mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
        }
        parmakizidurumux = "false";
        parmakizivepinx = "false";
        String Parmakizidurumu = GlobalKod.readSharedPreference(Parmakizi.this, "parmakizidurumu", "false");
        String Parmakizivepin = GlobalKod.readSharedPreference(Parmakizi.this, "parmakizivepin", "false");
        if (Build.VERSION.SDK_INT < 23) {
            parmakizi.setClickable(false);
            parmakizivepin.setClickable(false);
        }

        if (Parmakizidurumu.equals("true")) {
            parmakizi.setChecked(true);
            parmakizivepin.setClickable(false);
            parmakizidurumux = "true";
        } else if (Parmakizivepin.equals("true")) {
            parmakizivepin.setChecked(true);
            parmakizi.setClickable(false);
            parmakizivepinx = "true";
        }

        parmakizi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (parmakizi.isChecked()) {
                    parmakizidurumux = "true";
                    parmakizivepin.setClickable(false);
                    global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Confirm_with_fingerprint));
                } else {
                    parmakizidurumux = "false";
                    parmakizivepin.setClickable(true);
                    global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Confirm_with_fingerprint));
                }
            }
        });


        parmakizivepin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (parmakizivepin.isChecked()) {
                    parmakizivepinx = "true";
                    parmakizi.setClickable(false);
                    global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Confirm_with_fingerprint));
                } else {
                    parmakizivepinx = "false";
                    parmakizi.setClickable(true);
                    global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Confirm_with_fingerprint));
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        //start finger print authentication
        if (Build.VERSION.SDK_INT >= 23) {

            mFingerPrintAuthHelper.startAuth();

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 23) {

            mFingerPrintAuthHelper.stopAuth();

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
        GlobalKod.writeSharedPreference(Parmakizi.this, "parmakizidurumu", parmakizidurumux);
        GlobalKod.writeSharedPreference(Parmakizi.this, "parmakizivepin", parmakizivepinx);
        global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.Approved));
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                global.ToastOlustur(getApplicationContext(), getResources().getString(R.string.Your_fingerprint_was_not_recognized));
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                //global.ToastOlustur(getApplicationContext(),"Parmak izi tanıma işlemi başlatılamadı");
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:

                break;
        }


    }

}
