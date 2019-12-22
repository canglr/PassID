package net.gulernet.app.nxfolder.UiClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;


import net.gulernet.app.nxfolder.BgClass.Crypto;
import net.gulernet.app.nxfolder.BgClass.GlobalKod;
import net.gulernet.app.nxfolder.DataClass.ZipEleman;
import net.gulernet.app.nxfolder.R;

import java.io.File;
import java.util.List;


import static android.support.v4.util.Preconditions.checkNotNull;
import static net.gulernet.app.nxfolder.BgClass.DosyaBoyutu.convertDosyaBoyutu;
import static net.gulernet.app.nxfolder.UiClass.FragmentOne.ZipListe;

/**
 * Created by necip on 5.11.2017.
 */

public class OzelAdapter extends BaseAdapter {

    public LayoutInflater mInflater;
    public List<ZipEleman>     mZipListe;

    public OzelAdapter(Activity activity, List<ZipEleman> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mZipListe = ZipListe;
    }

    @Override
    public int getCount() {
        return mZipListe.size();
    }

    @Override
    public ZipEleman getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mZipListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.dosya_list_item, null);
        TextView ZipAdi =
                (TextView) satirView.findViewById(R.id.zipadi);
        TextView ZipBoyutu =
                (TextView) satirView.findViewById(R.id.zipboyutu);

        ZipEleman ZipElemann = mZipListe.get(position);

        ZipAdi.setText(ZipElemann.getZipadi());
        ZipBoyutu.setText(convertDosyaBoyutu(ZipElemann.getZipboyutu()));
        //String uzanti = DosyaUzantısı(ZipElemann.getZipadi());
        String mail = Crypto.md5(GlobalKod.readSharedPreference(satirView.getContext(),"mail",""));
        ImageView img= (ImageView) satirView.findViewById(R.id.imageView);
        File thumnailyolu = new File(satirView.getContext().getFilesDir()+"/thumbnail/"+mail+"/"+ZipElemann.getZipadi());
        if(GlobalKod.ResimUzantıKontrol(ZipElemann.getZipadi()) == true)
        {
            //Bitmap bmImg = BitmapFactory.decodeFile(satirView.getContext().getFilesDir()+"/thumbnail/"+ZipElemann.getZipadi());
            //img.setImageBitmap(bmImg);
            GlobalKod.loadImage(thumnailyolu,img);
        }else if(GlobalKod.VideoUzantıKontrol(ZipElemann.getZipadi()) == true){
            //Bitmap bmImg = BitmapFactory.decodeFile(satirView.getContext().getFilesDir()+"/thumbnail/"+ZipElemann.getZipadi());
            //img.setImageBitmap(bmImg);
            GlobalKod.loadImage(thumnailyolu,img);
        }
        else{
            img.setImageResource(R.drawable.nothumbnail);
        }

        return satirView;
    }


}