package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MerchantInventoryActivity extends Activity {

    DBmerchantItemsAdapter merchHelper;
    DBplayerItemsAdapter playerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_inventory);
        hideSystemUI();

        merchHelper = new DBmerchantItemsAdapter(this);
        playerHelper = new DBplayerItemsAdapter(this);
        GridView inventoryGridView = (GridView) findViewById(R.id.inventory_gridView_my_stuff);
        GridView merchantGridView = (GridView) findViewById(R.id.inventory_gridView_merchant);

        inventoryGridView.setAdapter(new MyStuffAdapter(this, (int) playerHelper.getTaskCount()));
        merchantGridView.setAdapter(new MerchantAdapter(this));


        inventoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void merchantInventoryBackbuttonPressed(View view){
        super.onBackPressed();
        finish();
    }






    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }







    private class MyStuffAdapter extends BaseAdapter {
        private Context mContext;
        private int num;
        //private final int num = (int) playerHelper.getTaskCount();


        public MyStuffAdapter(Context c, int number) {

            mContext = c;
            num = number;
        }

        public int getCount() {return num;}

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            if( playerHelper.getItemName(position + 1).equals("NOT_USED")) imageView.setImageResource(R.mipmap.ic_launcher);
            return imageView;
        }
    }






    private class MerchantAdapter extends BaseAdapter {
        private Context mContext;
        private final int num = (int) merchHelper.getTaskCount();

        public MerchantAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return num;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            if( merchHelper.getItemName(position+1).equals("NOT_USED")) imageView.setImageResource(R.mipmap.ic_launcher);
            else imageView.setImageResource(R.mipmap.ic_backbutton);
            //imageView.setAdjustViewBounds(true) - bis zur Grenze der Gridansicht (?)
            return imageView;
        }
    }
}



