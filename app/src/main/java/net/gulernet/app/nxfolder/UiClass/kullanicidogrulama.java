package net.gulernet.app.nxfolder.UiClass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class kullanicidogrulama extends AppCompatActivity {
    Button kontrolbtn;
    EditText kod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kullanicidogrulama);
        kontrolbtn = (Button) findViewById(R.id.kontrolileri);
        kod = (EditText) findViewById(R.id.kod);
        kontrolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c1 = kod.getText().toString();

                    String mail =  GlobalKod.readSharedPreference(getApplicationContext(),"mail","");
                    GlobalKod.BgKullaniciDogrulama task = new GlobalKod.BgKullaniciDogrulama(getApplicationContext(),mail,c1);
                    task.execute();


            }

        });





    }
}
