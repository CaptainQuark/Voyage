package com.example.thomas.voyage;

import android.content.Context;

import com.example.thomas.voyage.ItemPool;


public class Item {

    Context c;

    public Item(Context context){
        c = context;
        this.initializeItemByPool();
    }

    private void initializeItemByPool(){
        ItemPool ip = new ItemPool(c);
    }
}
