package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.widget.Toast;

public class Msg {
    public static void msg(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void msgShort(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
