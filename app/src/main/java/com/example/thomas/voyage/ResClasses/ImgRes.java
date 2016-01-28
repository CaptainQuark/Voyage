package com.example.thomas.voyage.ResClasses;

import android.content.Context;
import android.util.Log;

import com.example.thomas.voyage.ContainerClasses.Message;

public class ImgRes {

    private static int res = 0;
    private static final String ID_0 = "0", ID_1 = "merch", ID_2 = "hero";

    public static int res (Context c, String id, String param1){

        switch (id){
            case ID_0:
                break;
            case ID_1:
                res = c.getResources().getIdentifier("merchant_" + param1, "mipmap", c.getPackageName());
                break;
            case ID_2:
                res = c.getResources().getIdentifier(param1, "mipmap", c.getPackageName());
                break;
            default:
                Log.e("ERROR @ ", "ImgRes, default @ switch called");
        }


        return res;
    }
}
