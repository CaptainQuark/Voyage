package com.example.thomas.voyage.ContainerClasses;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;
import android.content.res.AssetManager;

import com.opencsv.CSVReader;

import java.io.FileReader;

public class CSVFileReader
{
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception
    {
        //Build reader instance
        //Read data.csv
        //Default seperator is comma
        //Default quote character is double quote
        //Start reading from line number 2 (line numbers start from zero)
        CSVReader reader = new CSVReader(new FileReader("HeroPrimaryTable.csv"), ',' , '"' , 1);

        //Read CSV line by line and use the string array as you want
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
                //Verifying the read data here
                System.out.println(Arrays.toString(nextLine));
            }
        }
    }
}
