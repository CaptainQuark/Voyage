package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Thomas on 21-Jul-15.
 */
public class Message {
    public static void message (Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
