package net.gulernet.app.nxfolder.UiClass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;

public class geribildirim extends AppCompatActivity {

    TextView sayac;
    EditText geribildirim;
    Button gonder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geribildirim);
        getSupportActionBar().setTitle(getResources().getString(R.string.Feedback));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sayac = (TextView) findViewById(R.id.sayac);
        geribildirim = (EditText) findViewById(R.id.geribildirim);
        gonder = (Button) findViewById(R.id.gonder);


        geribildirim.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});

        geribildirim.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sayac.setText(String.valueOf(geribildirim.getText().length())+"/500");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });



        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalKod.BgGeribildirim task = new GlobalKod.BgGeribildirim(getApplicationContext(),geribildirim.getText().toString());
                task.execute();


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
