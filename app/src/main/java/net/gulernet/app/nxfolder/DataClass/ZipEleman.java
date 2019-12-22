package net.gulernet.app.nxfolder.DataClass;

/**
 * Created by necip on 5.11.2017.
 */

public class ZipEleman {
    private String  zipadi;
    private long zipboyutu;


    public ZipEleman(String zipadi, long zipboyutu) {
        super();
        this.zipadi = zipadi;
        this.zipboyutu = zipboyutu;
    }

    @Override
    public String toString() {
        return zipadi;
    }

    public String getZipadi() {
        return zipadi;
    }

    public void setZipadi(String zipadi) {
        this.zipadi = zipadi;
    }

    public long getZipboyutu() {
        return zipboyutu;
    }

    public void setZipboyutu(long zipboyutu) {
        this.zipboyutu = zipboyutu;
    }


}
