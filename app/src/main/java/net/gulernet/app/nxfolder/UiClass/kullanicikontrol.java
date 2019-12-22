package net.gulernet.app.nxfolder.UiClass;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class kullanicikontrol extends AppCompatActivity {
    Button kontrolbtn;
    Button kodmevcut;
    Button googlekodmevcut;
    Button googlehesabi;
    Button diger;
    Button googlekontrolileri;
    EditText mail;
    String SecilenMail;
    LinearLayout googlelayout;
    LinearLayout digerlayout;
    private String[] menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kullanicikontrol);
        kontrolbtn = (Button) findViewById(R.id.kontrolileri);
        kodmevcut = (Button) findViewById(R.id.kodmevcut);
        googlekodmevcut = (Button) findViewById(R.id.googlekodmevcut);
        googlehesabi = (Button) findViewById(R.id.google);
        diger = (Button) findViewById(R.id.diger);
        googlekontrolileri = (Button) findViewById(R.id.googlekontrolileri);
        mail = (EditText) findViewById(R.id.mail);
        googlelayout = (LinearLayout) findViewById(R.id.GoogleLayout);
        digerlayout = (LinearLayout) findViewById(R.id.AnaLayout);
        final Spinner mailadresleri=(Spinner) findViewById(R.id.maillistesi);
        final GlobalKod global = new GlobalKod();



        googlehesabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT < 23) {
                    googlelayout.setVisibility(View.VISIBLE);
                    digerlayout.setVisibility(View.GONE);

                    menu = GlobalKod.GoogleMailListesi(getApplicationContext()).toArray(new String[GlobalKod.GoogleMailListesi(getApplicationContext()).size()]);
                    ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, android.R.id.text1, menu);

                    mailadresleri.setAdapter(adapter);

                    mailadresleri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            SecilenMail = mailadresleri.getSelectedItem().toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                }

                if (Build.VERSION.SDK_INT >= 23) {
//kullanıcıdan izin isteme metodu !
//Check whether your app has access to the READ permission//

                    if (checkPermission()) {
                        googlelayout.setVisibility(View.VISIBLE);
                        digerlayout.setVisibility(View.GONE);
//If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat//
                        menu = GlobalKod.GoogleMailListesi(getApplicationContext()).toArray(new String[GlobalKod.GoogleMailListesi(getApplicationContext()).size()]);
                        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_item, android.R.id.text1, menu);

                        mailadresleri.setAdapter(adapter);

                        mailadresleri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                SecilenMail = mailadresleri.getSelectedItem().toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });

                    } else {
                        //If your app doesn’t have permission to access external storage, then call requestPermission//
                                requestPermission();

                    }
                }




            }
        });




        diger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                digerlayout.setVisibility(View.VISIBLE);
                googlelayout.setVisibility(View.GONE);
                mail.requestFocus();

            }
        });






        if(GlobalKod.readSharedPreference(getApplicationContext(),"mail","").equals(""))
        {
            kodmevcut.setVisibility(View.GONE);
            googlekodmevcut.setVisibility(View.GONE);
        }

        kontrolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = mail.getText().toString();
                if(GlobalKod.EmailKontrol(c1) == false)
                {
                    mail.setError(getResources().getString(R.string.invalid_email_address));
                }else{
                    if(GlobalKod.pinyonlendirme)
                    {
                        if(GlobalKod.readSharedPreference(getApplicationContext(),"mail","").equals(c1))
                        {
                            GlobalKod.writeSharedPreference(getApplicationContext(),"mail",c1);
                            GlobalKod.BgKullaniciKontrol task = new GlobalKod.BgKullaniciKontrol(getApplicationContext(),c1);
                            task.execute();
                        }else{
                            mail.setError(getResources().getString(R.string.your_email_account_does_not));
                        }

                    }else{
                        GlobalKod.writeSharedPreference(getApplicationContext(),"mail",c1);
                        GlobalKod.BgKullaniciKontrol task = new GlobalKod.BgKullaniciKontrol(getApplicationContext(),c1);
                        task.execute();
                    }


                }


            }

            });


        kodmevcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dogrulama = new Intent(kullanicikontrol.this, net.gulernet.app.nxfolder.UiClass.kullanicidogrulama.class);
                startActivity(dogrulama);
                finish();

            }
        });

        googlekodmevcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dogrulama = new Intent(kullanicikontrol.this, net.gulernet.app.nxfolder.UiClass.kullanicidogrulama.class);
                startActivity(dogrulama);
                finish();

            }
        });






        googlekontrolileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = SecilenMail;
                if(GlobalKod.EmailKontrol(c1) == false)
                {
                    global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.invalid_email_address));
                }else{
                    if(GlobalKod.pinyonlendirme)
                    {
                        if(GlobalKod.readSharedPreference(getApplicationContext(),"mail","").equals(c1))
                        {
                            GlobalKod.writeSharedPreference(getApplicationContext(),"mail",c1);
                            GlobalKod.BgGoogleKullaniciKontrol task = new GlobalKod.BgGoogleKullaniciKontrol(getApplicationContext(),c1);
                            task.execute();
                        }else{
                            global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.your_email_account_does_not));
                        }

                    }else{
                        GlobalKod.writeSharedPreference(getApplicationContext(),"mail",c1);
                        GlobalKod.BgGoogleKullaniciKontrol task = new GlobalKod.BgGoogleKullaniciKontrol(getApplicationContext(),c1);
                        task.execute();
                    }


                }


            }

        });





    }



    private boolean checkPermission() {

//Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(kullanicikontrol.this, Manifest.permission.GET_ACCOUNTS);

//If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

//If the app doesn’t have this permission, then return false//

            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, 133);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        GlobalKod global = new GlobalKod();
        if (Build.VERSION.SDK_INT >= 23) {

            switch (requestCode) {
                case 133:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        googlelayout.setVisibility(View.VISIBLE);
                        digerlayout.setVisibility(View.GONE);

                        final Spinner mailadresleri=(Spinner) findViewById(R.id.maillistesi);
                        menu = GlobalKod.GoogleMailListesi(getApplicationContext()).toArray(new String[GlobalKod.GoogleMailListesi(getApplicationContext()).size()]);
                        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_item, android.R.id.text1, menu);

                        mailadresleri.setAdapter(adapter);

                        mailadresleri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                SecilenMail = mailadresleri.getSelectedItem().toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        });


                    } else {
                        digerlayout.setVisibility(View.VISIBLE);
                        googlelayout.setVisibility(View.GONE);
                        mail.requestFocus();
                        global.ToastOlustur(getApplicationContext(),getResources().getString(R.string.please_enter_your_email_address));
                    }
                    break;
            }
        }
    }




}
