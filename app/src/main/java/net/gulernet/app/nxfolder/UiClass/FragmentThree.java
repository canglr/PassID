package net.gulernet.app.nxfolder.UiClass;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;


/**
 * Created by necip on 30.10.2017.
 */

public class FragmentThree extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button buton;
    GlobalKod globalKod = new GlobalKod();

    public FragmentThree() {
        // Required empty public constructor
    }

    private String[] menu;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_three, container, false);

        ListView listemiz=(ListView) view.findViewById(R.id.listView1);

        //(B) adımı
        menu = getResources().getStringArray(R.array.menu);
        ArrayAdapter<String> adapter = new  ArrayAdapter<String>(getContext(),
                R.layout.simple_list_item_1, android.R.id.text1, menu);

        //(C) adımı
        listemiz.setAdapter(adapter);

        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

              if(menu[position].equals(getResources().getString(R.string.Account)))
              {
                  Intent Hesap = new Intent(getContext(), net.gulernet.app.nxfolder.UiClass.Hesap.class);
                  startActivity(Hesap);
                  getActivity().finish();
              }else if(menu[position].equals(getResources().getString(R.string.finger_print)))
              {
                  Intent Parmakizi = new Intent(getContext(), net.gulernet.app.nxfolder.UiClass.Parmakizi.class);
                  startActivity(Parmakizi);
                  getActivity().finish();
              }else if(menu[position].equals(getResources().getString(R.string.Feedback)))
              {
                  Intent Geribildirim = new Intent(getContext(), net.gulernet.app.nxfolder.UiClass.geribildirim.class);
                  startActivity(Geribildirim);
                  getActivity().finish();
              }else{

              }



            }
        });

        //Log.i("Bilgi", preferences.getString("kutusifre", ""));

        return view;
    }



}