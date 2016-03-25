package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.util.Log;

import com.example.thomas.voyage.R;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelperCSV {
    private Context c;

    public HelperCSV(Context c){

        this.c = c;
    }

    public List<String[]> getDataList(String fileName){

        List<String[]> fileLinesList = new ArrayList<>();

        try{
            CSVReader reader = new CSVReader(new InputStreamReader(c.getResources().openRawResource(
                    c.getResources().getIdentifier(fileName, "raw", c.getPackageName()))), ',');
            String [] nextLine;

            //Header wird verworfen
            reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                fileLinesList.add(nextLine);
            }

        }catch(java.io.FileNotFoundException f){
            Msg.msg(c, "Error : " + String.valueOf(f));
        }catch(java.io.IOException i){
            Msg.msg(c, "Error : " + String.valueOf(i));
        }

        return fileLinesList;
    }

    public String getString(String file, int line, String field){

        String var;
        int column = 0;
        int maxColumns = -1;
        List<String[]> table = new ArrayList<>();

        //maxColumns wird hardcoded festgelegt
        switch(file){
            case "monsterresourcetable":
                maxColumns = 20;
                break;
            case "heroresourcetable":
                maxColumns = 16;
                break;
            default:
                Log.e("HelperCSV: ", "Error@Switch for maxColumns!");
        }

        //CSV-Tabelle wird in eine Liste gelesen
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(c.getResources().openRawResource(
                    c.getResources().getIdentifier(file, "raw", c.getPackageName()))), ',');
            String [] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                table.add(nextLine);
            }

        }catch(java.io.FileNotFoundException f){
            Msg.msg(c, "Error : " + String.valueOf(f));
        }catch(java.io.IOException i){
            Msg.msg(c, "Error : " + String.valueOf(i));
        }

        //Erkennt die richtige Spalte anhand des mitgegebenen Strings field
        for (int k = 0; k < maxColumns; k++){
            if ((table.get(0)[k]).equals(field)){
                column = k;
            }
        }

        //Header wird verworfen, damit die Zeile mit der mitgegebenen Variable 'line' übereinstimmt
        table.remove(0);

        var = table.get(line)[column];

        if(var == null){
            Log.e("ERROR:", "Error@HelperCSV");
        }

        return var;
    }
}
