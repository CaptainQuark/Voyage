package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.thomas.voyage.ResClasses.ConstRes;

public class HelperSharedPrefs {

    public static long getCurrentMoney(Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    public static long addToCurrentMoneyAndGetNewVal(long money, Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) + money).apply();
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    public static long removeFromCurrentMoneyAndGetNewVal(long money, Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) - money).apply();
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    public static int setNewMerchantSlaveIdAndReturnVal(Context con, ConstRes c){
        SharedPreferences prefsMerchant = con.getSharedPreferences(c.SP_SLAVE_MERCHANT, Context.MODE_PRIVATE);
        prefsMerchant.edit().putInt(c.MERCHANT_SLAVE_ID, (prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0) + 1) % 4).apply();
        return prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0);
    }

    public static int getMerchantSlaveId(Context con, ConstRes c){
        SharedPreferences prefsMerchant = con.getSharedPreferences(c.SP_SLAVE_MERCHANT, Context.MODE_PRIVATE);
        return prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0);
    }
}
