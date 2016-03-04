package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.thomas.voyage.ResClasses.ConstRes;

public class HelperSharedPrefs {

    /*

    Geld des Spielers

     */

    public long getCurrentMoney(Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    public long addToCurrentMoneyAndGetNewVal(long money, Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) + money).apply();
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    public long removeFromCurrentMoneyAndGetNewVal(long money, Context con, ConstRes c){
        SharedPreferences prefsFortune = con.getSharedPreferences(c.SP_CURRENT_MONEY_PREF, Context.MODE_PRIVATE);
        prefsFortune.edit().putLong(c.MY_POCKET, prefsFortune.getLong(c.MY_POCKET, 0) - money).apply();
        return prefsFortune.getLong(c.MY_POCKET, 0);
    }

    /*

    Helden-Händler

     */

    public int incrementMerchantSlaveId(Context con, ConstRes c){
        SharedPreferences prefsMerchant = con.getSharedPreferences(c.SP_SLAVE_MERCHANT, Context.MODE_PRIVATE);
        prefsMerchant.edit().putInt(c.MERCHANT_SLAVE_ID, (prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0) + 1) % 4).apply();
        return prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0);
    }

    public int getMerchantSlaveId(Context con, ConstRes c){
        SharedPreferences prefsMerchant = con.getSharedPreferences(c.SP_SLAVE_MERCHANT, Context.MODE_PRIVATE);
        return prefsMerchant.getInt(c.MERCHANT_SLAVE_ID, 0);
    }

    public long getMerchSlaveDaytime(Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences("TIME_TO_LEAVE_PREF", Context.MODE_PRIVATE);
        return prefs.getLong("merchToLeaveDaytime", -1);
    }

    public long getMerchSlaveChangeDate(Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences("TIME_TO_LEAVE_PREF", Context.MODE_PRIVATE);
        return prefs.getLong("merchChangeDate", -1);
    }

    public void setNewMerchSlaveDaytime(long val, Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences("TIME_TO_LEAVE_PREF", Context.MODE_PRIVATE);
        prefs.edit().putLong("merchToLeaveDaytime", val).apply();
    }

    public void setNewMerchChangeDate(long val, Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences("TIME_TO_LEAVE_PREF", Context.MODE_PRIVATE);
        prefs.edit().putLong("merchChangeDate", val).apply();
    }

    /*

    Start-Activity

     */

    public boolean getIsFirstStarted(Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences(c.SP_START_ACTIVITY, Context.MODE_PRIVATE);
        return prefs.getBoolean(c.IS_FIRST_RUN, true);
    }

    public static void setIsFirstStarted(boolean val, Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences(c.SP_START_ACTIVITY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(c.IS_FIRST_RUN, val).apply();
    }

    public boolean getQuickCombatFirstStarted(Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences(c.SP_START_ACTIVITY, Context.MODE_PRIVATE);
        return prefs.getBoolean(c.QUICK_COMBAT_FIRST_STARTED, true);
    }

    public static void setQuickCombatFirstStarted(boolean val, Context con, ConstRes c){
        SharedPreferences prefs = con.getSharedPreferences(c.SP_START_ACTIVITY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(c.QUICK_COMBAT_FIRST_STARTED, val).apply();
    }
}
