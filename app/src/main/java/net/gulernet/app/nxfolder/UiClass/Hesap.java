package net.gulernet.app.nxfolder.UiClass;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class Hesap extends AppCompatActivity {
    TextView mailicon;
    TextView mail;
    private String[] menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        mailicon = (TextView) findViewById(R.id.mailicon);
        mail = (TextView) findViewById(R.id.mail);
        mailicon.setTypeface(fontAwesomeFont);
        mailicon.setText(getResources().getString(R.string.font_awesome_mail));
        mail.setText(" "+ GlobalKod.readSharedPreference(getApplicationContext(),"mail",""));
        getSupportActionBar().setTitle(getResources().getString(R.string.Account));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listemiz=(ListView) findViewById(R.id.Hesaplist);

        //(B) adımı
        menu = getResources().getStringArray(R.array.hesapmenu);
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_list_item_1, android.R.id.text1, menu);

        //(C) adımı
        listemiz.setAdapter(adapter);


        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if(menu[position].equals(getResources().getString(R.string.change_pin)))
                {
                    Intent pindegistir = new Intent(getApplicationContext(), net.gulernet.app.nxfolder.UiClass.pindegistir.class);
                    startActivity(pindegistir);
                    finish();
                }else if(menu[position].equals(getResources().getString(R.string.Sign_out_from_devices)))
                {
                    new AlertDialog.Builder(Hesap.this,R.style.AlertDialog).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getResources().getString(R.string.exit))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    GlobalKod.BgOturumuKapat task = new GlobalKod.BgOturumuKapat(getApplicationContext());
                                    task.execute();

                                }
                            }).setNegativeButton(getResources().getString(R.string.no), null).show();


                }else if(menu[position].equals(getResources().getString(R.string.Log_out)))
                {
                    new AlertDialog.Builder(Hesap.this,R.style.AlertDialog).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getResources().getString(R.string.exit))
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    GlobalKod.OturumuKapat(getApplicationContext());

                                }
                            }).setNegativeButton(getResources().getString(R.string.no), null).show();

                }else{

                }



            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }

}
