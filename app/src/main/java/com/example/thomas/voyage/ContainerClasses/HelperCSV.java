package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;

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
                    c.getResources().getIdentifier(fileName, "raw", c.getPackageName()))));
            String [] nextLine;

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
}
