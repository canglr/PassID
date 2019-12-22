package net.gulernet.app.nxfolder.UiClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;



public class pindegistir extends AppCompatActivity {
    EditText eskipinsifre;
    EditText pinsifre;
    EditText pinsifretekrar;
    Button degistir;
    Button degistiriptal;
    RadioGroup radiogrubu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindegistir);
        eskipinsifre = (EditText) findViewById(R.id.eskipinsifre);
        pinsifre = (EditText) findViewById(R.id.pinsifre);
        pinsifretekrar = (EditText) findViewById(R.id.pinsifretekrar);
        degistir = (Button) findViewById(R.id.degistir);
        degistiriptal = (Button) findViewById(R.id.degistiriptal);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_pin));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        degistiriptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
            }

    });


        degistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String c2 = eskipinsifre.getText().toString();
                String c3 = pinsifre.getText().toString();
                String c4 = pinsifretekrar.getText().toString();

        if(c2.trim().length() < 4)
        {
            eskipinsifre.setError(getResources().getString(R.string.must_be_at_least_4_characters));
        }else if(c3.trim().length() < 4)
        {
            pinsifre.setError(getResources().getString(R.string.must_be_at_least_4_characters));
        }else if(c4.trim().length() < 4)
        {
            pinsifretekrar.setError(getResources().getString(R.string.must_be_at_least_4_characters));
        }else{

            radiogrubu=(RadioGroup)findViewById(R.id.sifreGroup);
            int secilenRadio=radiogrubu.getCheckedRadioButtonId();
            String pinsahte = GlobalKod.readSharedPreference(getApplicationContext(),"sahtepinsifre","");
            String pin = GlobalKod.readSharedPreference(getApplicationContext(),"pinsifre","");
            String secpin="";

            switch(secilenRadio) {
                case R.id.pin: {
                    secpin = GlobalKod.readSharedPreference(getApplicationContext(),"pinsifre","");
                    break;
                }
                case R.id.sahtepin: {
                    secpin = GlobalKod.readSharedPreference(getApplicationContext(),"sahtepinsifre","");
                    break;
                }
            }
            if(!c2.trim().equals(secpin)){
                eskipinsifre.setError(getResources().getString(R.string.old_pin_wrong));

            }else if(!c3.trim().equals(c4)){
                pinsifre.setError(getResources().getString(R.string.pin_fields_do_not_match));
                pinsifretekrar.setError(getResources().getString(R.string.pin_fields_do_not_match));
            }else if(c3.trim().equals(pinsahte)){
                pinsifre.setError(getResources().getString(R.string.pin_and_fake_pin_can_not_be_the_same));
            }
            else if(c3.trim().equals(pin)){
                pinsifre.setError(getResources().getString(R.string.pin_and_fake_pin_can_not_be_the_same));
            }
            else{

                switch(secilenRadio) {
                    case R.id.pin: {
                        GlobalKod.writeSharedPreference(getApplicationContext(),"pinsifre",c3);
                        break;
                    }
                    case R.id.sahtepin: {
                        GlobalKod.writeSharedPreference(getApplicationContext(),"sahtepinsifre",c3);
                        break;
                    }
                }
                GlobalKod.pingiris = false;
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
            }

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

}
