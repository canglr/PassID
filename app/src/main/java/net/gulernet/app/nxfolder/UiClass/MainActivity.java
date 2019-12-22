package net.gulernet.app.nxfolder.UiClass;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.onesignal.OneSignal;
import com.yandex.metrica.YandexMetrica;

import net.gulernet.app.nxfolder.BgClass.Crypto;
import net.gulernet.app.nxfolder.BgClass.Cryptotxt;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.ServiceClass.Dosyaaktar;
import net.gulernet.app.nxfolder.ServiceClass.KilitKontrolSistemi;

import net.gulernet.app.nxfolder.ServiceClass.OturumKontrol;
import net.gulernet.app.nxfolder.ServiceClass.ThumbnailKontrol;




import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //ekran kilitlenmez
        String key = GlobalKod.readSharedPreference(getApplicationContext(),"kutusifre","");
        String pin = GlobalKod.readSharedPreference(getApplicationContext(),"pinsifre","");
        String sahtepin = GlobalKod.readSharedPreference(getApplicationContext(),"sahtepinsifre","");
        String hosgeldiniz = GlobalKod.readSharedPreference(getApplicationContext(),"hosgeldiniz","");
        String oturum = GlobalKod.readSharedPreference(getApplicationContext(),"oturum","");

        GlobalKod.HashKontrol(getApplicationContext());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        if(GlobalKod.rastgele_oturum_id == null)
        {
            GlobalKod.rastgele_oturum_id = UUID.randomUUID().toString().replace("-", "");
        }

        GlobalKod.Metrika(getApplicationContext());

        //GlobalKod yeni = new GlobalKod();
        GlobalKod.BgSurumKontrol task = new GlobalKod.BgSurumKontrol(getApplicationContext());
        task.execute();

       if(pin.equals("") || sahtepin.equals(""))
        {
            Intent kontrol = new Intent(MainActivity.this, kontrolislemleri.class);
            startActivity(kontrol);
            finish();
        }else if(key.equals("") || oturum.equals(""))
        {
            Intent kullanicikontrol = new Intent(MainActivity.this, net.gulernet.app.nxfolder.UiClass.kullanicikontrol.class);
            startActivity(kullanicikontrol);
            finish();
        }else if(GlobalKod.pingiris == false)
        {
            Intent pingiris = new Intent(MainActivity.this, net.gulernet.app.nxfolder.UiClass.pingiris.class);
            startActivity(pingiris);
            finish();
        }else if(GlobalKod.pingiris == true)
        {
            GlobalKod.BgCihazKontrol task2 = new GlobalKod.BgCihazKontrol(getApplicationContext());
            task2.execute();

            final Handler handler = new Handler();
            Timer timer;


            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            GlobalKod.BgGirisKontrol task3 = new GlobalKod.BgGirisKontrol(getApplicationContext());
                            task3.execute();

                        }
                    });
                }
            };

            timer = new Timer();

            timer.schedule(timerTask, 0, 30000);

            startService(new Intent(getApplicationContext(), OturumKontrol.class));
            startService(new Intent(getApplicationContext(), KilitKontrolSistemi.class));

        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new FragmentOne(), getResources().getString(R.string.safe));
        //adapter.addFragment(new FragmentTwo(), "TEST");
        adapter.addFragment(new FragmentThree(), getResources().getString(R.string.settings));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        if(!GlobalKod.readSharedPreference(getApplicationContext(),"mail","").equals("")) {
            File VeriYoluOlustur = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/" + Crypto.md5(GlobalKod.readSharedPreference(getApplicationContext(), "mail", ""))).toString());
            VeriYoluOlustur.mkdirs();
        }




    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.AlertDialog).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getResources().getString(R.string.exit))
                .setMessage(getResources().getString(R.string.secure_log_out))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GlobalKod.guvenlicikis(getApplicationContext());
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton(getResources().getString(R.string.no), null).show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //farklı aç özelliğinde reklam göstermek için kullanıldı.
        GlobalKod.Reklamyukle(getApplicationContext());

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //farklı aç özelliğinde reklam göstermek için kullanıldı.
        if(GlobalKod.reklamgoster == 1)
        {
            GlobalKod.Reklamgoster();
            GlobalKod.reklamgoster = 0;
        }

    }


}









