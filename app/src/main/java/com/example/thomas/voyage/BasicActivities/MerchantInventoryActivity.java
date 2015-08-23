package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.Databases.DBmerchantItemsAdapter;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.ContainerClasses.Item;
import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class MerchantInventoryActivity extends Activity {

    private ConstRes co = new ConstRes();
    private DBmerchantItemsAdapter merchHelper;
    private DBplayerItemsAdapter playerHelper;
    private TextView buyView, dismissView;
    private GridView playerGridView, merchantGridView;

    // = UID der Tabelle und NICHT die Position innerhalb des Grids
    private int selectedItemUIDFromMerch = -1, selectedItemUIDfromPlayer = -1;
    private String lastSelectedUID = co.NOT_USED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_inventory);
        initializeViews();

        merchHelper = new DBmerchantItemsAdapter(this);
        playerHelper = new DBplayerItemsAdapter(this);

        playerGridView.setAdapter(new MyStuffAdapter(this, (int) playerHelper.getTaskCount()));
        merchantGridView.setAdapter(new MerchantAdapter(this));


        playerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                selectedItemUIDfromPlayer = position + 1;
                dismissView.setTextColor(Color.WHITE);
                Message.message(getApplicationContext(), playerHelper.getOneItemRow(position + 1) + "");
                lastSelectedUID = "player";
            }
        });

        merchantGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                selectedItemUIDFromMerch = position + 1;
                if (getPosOfFreeSlotInPlayerItemDatabase() != -1) {
                    buyView.setTextColor(Color.WHITE);
                }
                dismissView.setTextColor(Color.WHITE);
                lastSelectedUID = "merchant";
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

    public void merchantItemProfileTapped(View view){
        setNewItemMerchant();
        merchantGridView.invalidateViews();
        Message.message(this, "New Merchant");
    }

    public void buyItemButton(View view){

        if( getPosOfFreeSlotInPlayerItemDatabase() == -1){
            Message.message(this, "Kein freier Platz verfügbar!");

        }else if( selectedItemUIDFromMerch == -1 ){
            Message.message(this, "Kein Item ausgewählt!");

        }else{
            addOneItemToPlayerDatabase(selectedItemUIDFromMerch);
            buyView.setTextColor(getResources().getColor(R.color.standard_background));
            dismissItem("merchant", selectedItemUIDFromMerch);
            playerGridView.invalidateViews();
            merchantGridView.invalidateViews();


            selectedItemUIDFromMerch = -1;
            selectedItemUIDfromPlayer = -1;
            dismissView.setTextColor(getResources().getColor(R.color.standard_background));
            buyView.setTextColor(getResources().getColor(R.color.standard_background));
        }
    }

    public void dismissItemButton(View view){

        if(lastSelectedUID.equals("player") && selectedItemUIDfromPlayer != -1){
            dismissItem(lastSelectedUID, selectedItemUIDfromPlayer);
            selectedItemUIDfromPlayer = -1;
            playerGridView.invalidateViews();
            dismissView.setTextColor(getResources().getColor(R.color.standard_background));
        }
        else if(lastSelectedUID.equals("merchant") && selectedItemUIDFromMerch != -1){
            dismissItem(lastSelectedUID, selectedItemUIDFromMerch);
            selectedItemUIDFromMerch = -1;
            merchantGridView.invalidateViews();
            dismissView.setTextColor(getResources().getColor(R.color.standard_background));
            buyView.setTextColor(getResources().getColor(R.color.standard_background));
        }
    }

    private void dismissItem(String id, int pos){

        switch (id){
            case "player":
                playerHelper.markOneRowAsUnused(pos);
                break;
            case "merchant":
                merchHelper.markOneRowAsUnused(pos);
                break;
        }

    }

    private void setNewItemMerchant(){

        for(int i = 1; i <= merchHelper.getTaskCount(); i++){

            Item item = new Item(this);

            long validation = merchHelper.updateRowWithItemData(
                    i,
                    item.getStrings("ITEM_NAME"),
                    item.getInts("SKILL_ID"),
                    item.getStrings("DES_MAIN"),
                    item.getStrings("DES_ADD"),
                    item.getInts("BUY_COSTS"),
                    item.getInts("SPELL_COSTS"),
                    item.getStrings("RARITY"));

            if(validation < 0) Message.message(this, "ERROR @ insert @ setNewItemMerchant");
        }
    }

    private void addOneItemToPlayerDatabase(int merchPos){

        /*

        Es wird vorrausgesetzt, dass diese Funktion nur aufgerufen werden kann,
        wenn noch mind. ein freier Platz verfügbar ist.

         */

        long validation = playerHelper.updateRowWithItemData(
                getPosOfFreeSlotInPlayerItemDatabase(),
                merchHelper.getItemName(merchPos),
                merchHelper.getItemSkillsId(merchPos),
                merchHelper.getItemDescriptionMain(merchPos),
                merchHelper.getItemDescriptionAdditonal(merchPos),
                merchHelper.getItemBuyCosts(merchPos),
                merchHelper.getItemSpellCosts(merchPos),
                merchHelper.getItemRarity(merchPos));

        if(validation < 0) Message.message(this, "ERROR @ addOneItemToPlayerDatabase");
    }

    private int getPosOfFreeSlotInPlayerItemDatabase(){

        ConstRes co = new ConstRes();
        int pos = -1;

        for(int i = 1; i <= playerHelper.getTaskCount(); i++){

            if(playerHelper.getItemName(i).equals(co.NOT_USED)){
                pos = i;
                i = (int) playerHelper.getTaskCount() + 1;

            }else if( i == playerHelper.getTaskCount() ){
                Message.message(this, "Alle Plätze bereits belegt");
            }
        }

        return pos;
    }

    private int getNumberOfUsedSlots(String id){
        int count = 0;

        switch (id){

            case "player":

                for(int i = 1; i <= playerHelper.getTaskCount(); i++){
                    if(! playerHelper.getItemName(i).equals(co.NOT_USED) ){
                        count++;
                    }
                }
                break;

            case "merchant":

                for(int i = 1; i <= merchHelper.getTaskCount(); i++){
                    if(! merchHelper.getItemName(i).equals(co.NOT_USED) ){
                        count++;
                    }
                }
                break;
        }

        return count;
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

            if( !playerHelper.getItemName(position + 1).equals(co.NOT_USED)) imageView.setImageResource(R.mipmap.ic_launcher);
            else{
                imageView.setImageResource(R.mipmap.ic_dot);
            }
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

            if( !merchHelper.getItemName(position+1).equals(co.NOT_USED)){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }else{
                imageView.setImageResource(R.mipmap.ic_dot);
            }
            //imageView.setAdjustViewBounds(true) - bis zur Grenze der Gridansicht (?)
            return imageView;
        }
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

    private void initializeViews(){
        hideSystemUI();
        playerGridView = (GridView) findViewById(R.id.inventory_gridView_my_stuff);
        merchantGridView = (GridView) findViewById(R.id.inventory_gridView_merchant);
        buyView = (TextView)findViewById(R.id.invetory_textView_buy);
        dismissView = (TextView)findViewById(R.id.invetory_textView_dismiss);

        buyView.setTextColor(getResources().getColor(R.color.standard_background));
        dismissView.setTextColor(getResources().getColor(R.color.standard_background));

    }
}



