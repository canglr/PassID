package net.gulernet.app.nxfolder.DataClass;

/**
 * Created by necip on 29.12.2017.
 */

public class SurumController {

    public static String surumkodu="";


    public static void Surum(String surum){
        surumkodu = surum;
    }

    public static String Surum(){
        return surumkodu;
    }

}
