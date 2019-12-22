package net.gulernet.app.nxfolder.UiClass;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.gulernet.app.nxfolder.BgClass.Crypto;
import net.gulernet.app.nxfolder.BgClass.Dosyaislemleri;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.DataClass.ZipEleman;
import net.gulernet.app.nxfolder.BgClass.DosyaBoyutu;
import net.gulernet.app.nxfolder.R;
import net.gulernet.app.nxfolder.ServiceClass.Dosyaaktar;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import javax.crypto.Cipher;

import static android.app.Activity.RESULT_OK;


/**
 * Created by necip on 30.10.2017.
 */

public class FragmentOne extends Fragment {
    private String  rastgele_id = UUID.randomUUID().toString().replace("-", "");
    Button buton;
    Button yenile;
    Button yeniklasor;
    Button klasorduzenle;
    Button klasorsil;
    TextView yazi;
    Spinner klasorler;
    ArrayAdapter<String> adapterklasor;
    public static ArrayList<String> adres;
    ListView listemiz;
    private ProgressDialog progressDialog;
    protected static List<ZipEleman> ZipListe = new ArrayList<ZipEleman>();
    long KasaBoyutu;
    String DosyaAdi;
    public static String SecilenKlasor;
GlobalKod globalkod = new GlobalKod();
    public FragmentOne() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_one, container, false);
        File yol = getContext().getFilesDir();
        yazi = (TextView) view.findViewById(R.id.yazi);
        //yazi.setText(DosyaBoyutu.convertDosyaBoyutu(KasaBoyutu));
        //Log.i("Bilgi", DosyaBoyutu.convertDosyaBoyutu(OzelAdapter.KasaBoyutu));
        buton = (Button) view.findViewById(R.id.button);
        yenile = (Button) view.findViewById(R.id.yenile);
        listemiz= view.findViewById(R.id.ZipListe);
        yeniklasor= view.findViewById(R.id.yeniklasor);
        klasorduzenle= view.findViewById(R.id.klasorduzenle);
        klasorsil= view.findViewById(R.id.klasorsil);


        Typeface fontAwesomeFont = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
        buton.setTypeface(fontAwesomeFont);
        yenile.setTypeface(fontAwesomeFont);
        yeniklasor.setTypeface(fontAwesomeFont);
        klasorduzenle.setTypeface(fontAwesomeFont);
        klasorsil.setTypeface(fontAwesomeFont);
        yazi.setTypeface(fontAwesomeFont);


        //(B) adımı

        //ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, ZipListe);
        OzelAdapter adapter=new OzelAdapter(getActivity(), ZipListe);
        //(C) adımı
        listemiz.setAdapter(adapter);
        klasorler = (Spinner)view.findViewById(R.id.klasorlistesi);
        KlasorListesiYenile();

        ZipListe.clear();
        new BackgroundZipListesiİcerigi().execute((Void) null);

        klasorler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                SecilenKlasor = klasorler.getSelectedItem().toString();
                ZipListeYenile();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File folder = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))).toString());
                if(GlobalKod.KlasorSayisi(folder.toString()) != 0)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    //intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_file)), 1969);
                }else{
                    globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.create_a_folder),getResources().getString(R.string.at_least_one_folder),getResources().getString(R.string.ok));
                }




            }
        });


        listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                globalkod.ToastOlustur(getContext(),getResources().getString(R.string.hold_down_to_screen));
            }
        });

        listemiz.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                yenile.setEnabled(true);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    yenile.setEnabled(false);
                    //Log.i("SCROLLING DOWN","TRUE");
                }

                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    //Log.i("SCROLLING UP","TRUE");
                    yenile.setEnabled(false);
                }

                mLastFirstVisibleItem=firstVisibleItem;
            }

        });


        yenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZipListeYenile();
            }
        });


        yeniklasor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
                builder.setTitle(getResources().getString(R.string.new_folder));

// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Dosyaislemleri.KlasorKontrol(input.getText().toString(),getContext()))
                        {
                            globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.folder_could_not_be_created),getResources().getString(R.string.folder_with_same_name_exists),getResources().getString(R.string.ok));
                        }else {
                            Dosyaislemleri.KlasorOlustur(input.getText().toString(),getContext());
                            KlasorListesiYenile();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });



        klasorduzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SecilenKlasor != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
                builder.setTitle(getResources().getString(R.string.rename));

// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                input.setText(SecilenKlasor);

// Set up the buttons
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Dosyaislemleri.KlasorKontrol(input.getText().toString(),getContext()))
                        {
                            globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.can_not_rename_folder),getResources().getString(R.string.folder_with_same_name_exists),getResources().getString(R.string.ok));
                        }else {
                            Dosyaislemleri.KlasorDuzenle(SecilenKlasor, input.getText().toString(),getContext());
                            KlasorListesiYenile();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                }else{
                    globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.folder_not_selected),getResources().getString(R.string.the_folder_must_be_selected),getResources().getString(R.string.ok));
                }
            }
        });


        klasorsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(SecilenKlasor != null) {
                  new AlertDialog.Builder(getContext(),R.style.AlertDialog).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getResources().getString(R.string.confirmation))
                          .setMessage("'"+SecilenKlasor +"'"+" "+ getResources().getString(R.string.are_you_sure_you_want_to_delete_it))
                          .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {

                                  Dosyaislemleri.KlasorSil(SecilenKlasor,getContext());
                                  KlasorListesiYenile();


                              }
                          }).setNegativeButton(getResources().getString(R.string.cancel), null).show();
              }else{
                  globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.folder_not_selected),getResources().getString(R.string.the_folder_must_be_selected_deletion),getResources().getString(R.string.ok));
              }
            }
        });
        registerForContextMenu(listemiz);
        return view;

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle(getResources().getString(R.string.options));
        menu.add(0, v.getId(), 0, getResources().getString(R.string.open));
        menu.add(0, v.getId(), 0, getResources().getString(R.string.open_differently));
        menu.add(0,v.getId(),0,getResources().getString(R.string.export));
        menu.add(0,v.getId(),0,getResources().getString(R.string.rename));
        menu.add(0,v.getId(),0,getResources().getString(R.string.delete));
    }


    @Override    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Object o = listemiz.getItemAtPosition(info.position);
        if(item.getTitle()==getResources().getString(R.string.open)){
            DosyaAdi = o.toString();
            new BackgroundZipDosyaAc().execute((Void) null);
        }
        else if(item.getTitle()==getResources().getString(R.string.open_differently)){
            DosyaAdi = o.toString();
            GlobalKod.reklamgoster = 1;
            new BackgroundZipDosyaFarkliAc().execute((Void) null);
        }
        else if (item.getTitle()==getResources().getString(R.string.export)){
            DosyaAdi = o.toString();
            new BackgroundZipDosyaDisariAktar().execute((Void) null);
        }
        else if (item.getTitle()==getResources().getString(R.string.rename)){
            DosyaAdi = o.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
            builder.setTitle(getResources().getString(R.string.rename));

// Set up the input
            final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            input.setText(DosyaAdi);

// Set up the buttons
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Dosyaislemleri.KlasorKontrol(input.getText().toString(),getContext()))
                    {
                        globalkod.AlertDialogOlustur(getContext(),getResources().getString(R.string.can_not_rename_file),getResources().getString(R.string.file_with_the_same_name_exists),getResources().getString(R.string.ok));
                    }else {
                        Dosyaislemleri.DosyaDuzenle(getContext(),SecilenKlasor,DosyaAdi, input.getText().toString());
                        ZipListeYenile();
                    }
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }
        else if(item.getTitle()==getResources().getString(R.string.delete)){
            DosyaAdi = o.toString();
            new AlertDialog.Builder(getContext(),R.style.AlertDialog).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getResources().getString(R.string.confirmation))
                    .setMessage("'"+DosyaAdi+"'"+" "+getResources().getString(R.string.are_you_sure_you_want_to_delete_it))
                    .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ZipDosyaSil(DosyaAdi);
                            globalkod.ToastOlustur(getContext(),DosyaAdi+" "+getResources().getString(R.string.deleted));
                            ZipListeYenile();


                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel), null).show();


        }
        return super.onContextItemSelected(item);
    }

    public void KlasorListesiYenile(){
        Dosyaislemleri.klasorlistesi.clear();
        Dosyaislemleri.KlasorListele(getContext());
        adapterklasor = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, Dosyaislemleri.klasorlistesi);
        klasorler.setAdapter(adapterklasor);
    }

    public void ZipListeYenile(){

        final int index = listemiz.getFirstVisiblePosition();
        ZipListe.clear();
        new BackgroundZipListesiİcerigi().execute((Void) null);
        OzelAdapter adapter=new OzelAdapter(getActivity(), ZipListe);
        listemiz.clearFocus();
        listemiz.post(new Runnable() {
            @Override
            public void run() {
                listemiz.setSelection(index);
            }
        });
        //listemiz.setAdapter(adapter);

    }



    public void BgZipListeYenile(){

        OzelAdapter adapter=new OzelAdapter(getActivity(), ZipListe);
        listemiz.setAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1969 && resultCode==RESULT_OK) {
            adres = new ArrayList<String>();
            ClipData clipData = data.getClipData();
            if(clipData == null){

                adres.add(GlobalKod.getPathFromUri(getContext(),data.getData()));
                //new BackgroundZipİceAktar().execute((Void) null);
                //getContext().startService(new Intent(getContext(), Dosyaaktar.class));
                Intent msgIntent = new Intent(getContext(), Dosyaaktar.class);
                msgIntent.putStringArrayListExtra("adres",adres);
                msgIntent.putExtra("SecilenKlasor",SecilenKlasor);
                getContext().startService(msgIntent);

            }else {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    adres.add(GlobalKod.getPathFromUri(getContext(),uri));
                }
                //new BackgroundZipİceAktar().execute((Void) null);
                //getContext().startService(new Intent(getContext(), Dosyaaktar.class));
                Intent msgIntent = new Intent(getContext(), Dosyaaktar.class);
                msgIntent.putStringArrayListExtra("adres",adres);
                msgIntent.putExtra("SecilenKlasor",SecilenKlasor);
                getContext().startService(msgIntent);
            }

        }
    }




    public void ZipListesiİcerigi() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String key = Crypto.md5(preferences.getString("kutusifre", ""));
        try {
            File folder = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor).toString());
            File[] listOfFiles = folder.listFiles();
            KasaBoyutu = 0;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                        //System.out.println("File " + listOfFiles[i].getName()+" - "+listOfFiles[i].length());
                    if(file.length() != 0) {
                        ZipListe.add(new ZipEleman(file.getName(), file.length()));
                        KasaBoyutu += file.length();
                    }else
                    {
                        File gecersizdosya = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor+"/"+file.getName()).toString());
                        gecersizdosya.delete();
                    }
                }

            }


        } catch (Exception e) {
            Log.i("Bilgi","Hata Oluştu !");
            e.printStackTrace();
        }

    }



    public void ZipDosyaSil(String dosyaadi) {

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor+"/"+dosyaadi).toString());
            file.delete();
            String mail = Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""));
            File thumnail = new File(getActivity().getFilesDir()+"/thumbnail/"+mail+"/"+dosyaadi);
            thumnail.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void ZipDosyaDisariAktar(String dosyaadi) {

        try {
            String key = GlobalKod.readSharedPreference(getContext(),"kutusifre","");
            File encryptedFile = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor+"/"+dosyaadi).toString());
            File decryptedFile = new File(Environment.getExternalStoragePublicDirectory("/"+Environment.DIRECTORY_DOWNLOADS+"/"+dosyaadi).toString());
            Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Bilgi","Dışarı aktarılamıyor");
        }
    }

    public void ZipDosyaFarkliAc(String dosyaadi) {

        try {
            File VeriYoluOlustur = new File(getContext().getExternalFilesDir(null)+"/onbellek/");
            VeriYoluOlustur.mkdirs();
            String key = GlobalKod.readSharedPreference(getContext(),"kutusifre","");
            File encryptedFile = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor+"/"+dosyaadi).toString());
            File decryptedFile = new File(getContext().getExternalFilesDir(null)+"/onbellek/"+dosyaadi);
            Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);

            File temp_file=new File(getContext().getExternalFilesDir(null)+"/onbellek/"+DosyaAdi);
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(temp_file),getMimeType(temp_file.getAbsolutePath()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Bilgi","Dosya Açılamıyor");
        }
    }

    public void ZipDosyaAc(String dosyaadi) {

        try {

            File VeriYoluOlustur = new File(getContext().getCacheDir()+"/onbellek/");
            VeriYoluOlustur.mkdirs();
            String key = GlobalKod.readSharedPreference(getContext(),"kutusifre","");
            File encryptedFile = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor+"/"+dosyaadi).toString());
            File decryptedFile = new File(getContext().getCacheDir()+"/onbellek/"+dosyaadi);
            Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);

            File temp_file=new File(getContext().getCacheDir()+"/onbellek/"+DosyaAdi);

            //intent.setDataAndType(Uri.fromFile(temp_file),getMimeType(temp_file.getAbsolutePath()));


            String uzanti = GlobalKod.DosyaUzantısı(DosyaAdi);
            String [] image_type={"jpg","png","bmp","webp","jpeg","JPG","PNG","BMP","WEBP","JPEG"};
            String [] video_type={"mp4","3gp","webm","MP4","3GP","WEBM"};
            Boolean image_type_return = Arrays.asList(image_type).contains(uzanti);
            Boolean video_type_return = Arrays.asList(video_type).contains(uzanti);


            if(video_type_return) {
                video_play.Video_Url = temp_file.getAbsolutePath();
                video_play.Video_Dosyaadi = dosyaadi;
                Intent main = new Intent(getContext(), video_play.class);
                startActivity(main);
            }else if(image_type_return){
                image_play.İmage_url = temp_file.getAbsolutePath();
                Intent main = new Intent(getContext(), image_play.class);
                startActivity(main);
            }else{
                globalkod.BildirimOlustur(getContext(),getResources().getString(R.string.File_type_not_supported),getResources().getString(R.string.Try_different_Open));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Bilgi","Dosya Açılamıyor");
        }
    }

    private String getMimeType(String url)
    {
        String parts[]=url.split("\\.");
        String extension=parts[parts.length-1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }



    private class BackgroundZipDosyaDisariAktar extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.processing));
            progressDialog.setMessage(getResources().getString(R.string.extracting_file));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ZipDosyaDisariAktar(DosyaAdi);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            globalkod.ToastOlustur(getContext(),DosyaAdi+" "+Environment.DIRECTORY_DOWNLOADS+" "+getResources().getString(R.string.he_was_transferred));
            ZipListeYenile();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }
    }

    private class BackgroundZipDosyaFarkliAc extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.processing));
            progressDialog.setMessage(getResources().getString(R.string.opening_file));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ZipDosyaFarkliAc(DosyaAdi);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            ZipListeYenile();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }
    }

    private class BackgroundZipListesiİcerigi extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.processing));
            progressDialog.setMessage(getResources().getString(R.string.preparing_the_list));
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getActivity().runOnUiThread(new Runnable()  {
                public void run() {
            ZipListesiİcerigi();

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            File folder = new File(Environment.getExternalStoragePublicDirectory("/.GULERNET/PassID/0/"+Crypto.md5(GlobalKod.readSharedPreference(getContext(),"mail",""))+"/"+SecilenKlasor).toString());
            int dosyasayisi = GlobalKod.DosyaSayisi(folder.toString());
            if(KasaBoyutu == 0 && dosyasayisi == 0){
                yazi.setText(getResources().getString(R.string.the_file_could_not_be_found));
            }else{
                String klasor = getResources().getString(R.string.font_awesome_yeni_klasor);
                String dosya = getResources().getString(R.string.font_awesome_dosya_sayisi);
                yazi.setText(" "+klasor+" "+ DosyaBoyutu.convertDosyaBoyutu(KasaBoyutu)+" "+dosya+" " +dosyasayisi);
                BgZipListeYenile();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }
    }

    private class BackgroundZipDosyaAc extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(getResources().getString(R.string.processing));
            progressDialog.setMessage(getResources().getString(R.string.opening_file));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ZipDosyaAc(DosyaAdi);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            ZipListeYenile();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Integer currentProgress = values[0];
            progressDialog.setProgress(currentProgress);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            progressDialog.dismiss();
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String sonuc = intent.getStringExtra("dosyaaktar");
            String thumnail = intent.getStringExtra("thumnailkontrol");

            if(sonuc == null)
            {
                sonuc = "x";
            }else if(thumnail == null)
            {
                thumnail = "x";
            }else{

            }

            if(sonuc.equals("ok"))
            {
                ZipListeYenile();
            }else if(thumnail.equals("ok"))
            {
                ZipListeYenile();
            }


        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MAIN);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
        super.onStart();
    }

}










