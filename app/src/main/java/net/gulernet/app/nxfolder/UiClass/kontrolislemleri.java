package net.gulernet.app.nxfolder.UiClass;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class kontrolislemleri extends AppCompatActivity {
    Button kontrolbtn;
    EditText pinsifre;
    EditText pinsifretekrar;
    EditText sahtepinsifre;
    EditText sahtepinsifretekrar;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kontrolislemleri);
        pinsifre = (EditText) findViewById(R.id.pinsifre);
        pinsifretekrar = (EditText) findViewById(R.id.pinsifretekrar);
        sahtepinsifre = (EditText) findViewById(R.id.sahtepinsifre);
        sahtepinsifretekrar = (EditText) findViewById(R.id.sahtepinsifretekrar);
        kontrolbtn = (Button) findViewById(R.id.kontrolileri);


        kontrolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c3 = pinsifre.getText().toString();
                String c4 = pinsifretekrar.getText().toString();
                String c5 = sahtepinsifre.getText().toString();
                String c6 = sahtepinsifretekrar.getText().toString();

             if(c3.trim().length() < 4)
              {
                  pinsifre.setError(getResources().getString(R.string.must_be_at_least_4_characters));
              }else if(c4.trim().length() < 4)
              {
                  pinsifretekrar.setError(getResources().getString(R.string.must_be_at_least_4_characters));
              }else if(c5.trim().length() < 4)
              {
                  sahtepinsifre.setError(getResources().getString(R.string.must_be_at_least_4_characters));
              }else if(c6.trim().length() <4)
              {
                  sahtepinsifretekrar.setError(getResources().getString(R.string.must_be_at_least_4_characters));
              }else{

                  if(!c3.trim().equals(c4)){
                      pinsifre.setError(getResources().getString(R.string.pin_fields_do_not_match));
                      pinsifretekrar.setError(getResources().getString(R.string.pin_fields_do_not_match));
                  }else if(!c5.trim().equals(c6))
                  {
                      sahtepinsifre.setError(getResources().getString(R.string.fake_pin_fields_do_not_match));
                      sahtepinsifretekrar.setError(getResources().getString(R.string.fake_pin_fields_do_not_match));
                  }else if(c3.trim().equals(c5)){
                      pinsifre.setError(getResources().getString(R.string.pin_and_fake_pin_can_not_be_the_same));
                      sahtepinsifre.setError(getResources().getString(R.string.pin_and_fake_pin_can_not_be_the_same));
                  }
                  else{
                      GlobalKod.writeSharedPreference(getApplicationContext(),"pinsifre",c3);
                      GlobalKod.writeSharedPreference(getApplicationContext(),"sahtepinsifre",c5);
                      Intent main = new Intent(kontrolislemleri.this, MainActivity.class);
                      startActivity(main);
                      finish();
                  }

              }

            }
        });




    }



}


