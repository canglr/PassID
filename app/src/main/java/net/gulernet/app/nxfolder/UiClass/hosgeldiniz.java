package net.gulernet.app.nxfolder.UiClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class hosgeldiniz extends AppCompatActivity {
TextView bilgi;
Button hosgeldinileri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hosgeldiniz);
        hosgeldinileri = (Button) findViewById(R.id.hosgeldinileri);
        bilgi = (TextView) findViewById(R.id.bilgi);
        bilgi.setSingleLine(false);
        bilgi.setText("Hoş geldiniz,\n"
        + "Bir sonraki adımda gerekli bilgileri tanımlayarak uygulamayı kullanmaya başlayabilirsiniz.\n"
        +"Verileriniz güvenli bir şekilde cihazınızda saklanacaktır.");


        hosgeldinileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalKod.writeSharedPreference(getApplicationContext(),"hosgeldiniz","1");
                Intent main = new Intent(hosgeldiniz.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        });



    }
}
