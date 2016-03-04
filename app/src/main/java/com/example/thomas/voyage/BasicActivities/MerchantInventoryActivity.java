package com.example.thomas.voyage.BasicActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.HelperSharedPrefs;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBmerchantItemsAdapter;
import com.example.thomas.voyage.Databases.DBplayerItemsAdapter;
import com.example.thomas.voyage.ContainerClasses.Item;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.Calendar;

public class MerchantInventoryActivity extends Activity {

    private ConstRes c = new ConstRes();
    private DBmerchantItemsAdapter merchHelper;
    private DBplayerItemsAdapter playerHelper;
    private TextView buyView, dismissView, itemNameView, itemDesMainView, itemDesAddView, itemRarityView, freeSlotsView, fortuneView, itemPriceView;
    private ImageView itemIconView;
    private GridView playerGridView, merchantGridView;
    private HelperSharedPrefs prefs = new HelperSharedPrefs();

    // = UID der Tabelle und NICHT die Position innerhalb des Grids
    private int selectedItemUIDFromMerch = -1, selectedItemUIDfromPlayer = -1;
    private String lastSelectedUID = c.NOT_USED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_inventory);
        initializeViews();
        iniValues();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }



    /*

    onClick-Methoden

     */



    public void merchantItemProfileTapped(View view){
        setNewItemMerchant();
        merchantGridView.invalidateViews();
        Msg.msg(this, "New Merchant");
    }

    public void buyItemButton(View view){

        if( getPosOfFreeSlotInPlayerItemDatabase() == -1){
            Msg.msg(this, "Kein freier Platz verfügbar!");

        }else if( selectedItemUIDFromMerch == -1 ){
            Msg.msg(this, "Kein Item ausgewählt!");

        }else if( prefs.getCurrentMoney(this, new ConstRes()) < merchHelper.getItemBuyCosts(selectedItemUIDFromMerch)){
            Msg.msg(this, "Nicht genug Vermögen vorhanden!");

        }else{
            addOneItemToPlayerDatabase(selectedItemUIDFromMerch);
            buyView.setTextColor(getColor(R.color.standard_background));
            playerGridView.invalidateViews();
            merchantGridView.invalidateViews();

            prefs.removeFromCurrentMoneyAndGetNewVal(merchHelper.getItemBuyCosts(selectedItemUIDFromMerch), this, c);

            fortuneView.setText("$" + prefs.getCurrentMoney(this, new ConstRes()));
            freeSlotsView.setText(getNumberOfUsedSlots("player") + "/" + playerHelper.getTaskCount() + " Plätzen belegt");
            itemDesMainView.setText("");
            itemDesAddView.setText("");
            itemNameView.setText("");
            itemRarityView.setText("");
            itemPriceView.setText("");

            dismissItem("merchant", selectedItemUIDFromMerch);
            selectedItemUIDFromMerch = -1;
            selectedItemUIDfromPlayer = -1;
            dismissView.setTextColor(getColor(R.color.standard_background));
            buyView.setTextColor(getColor(R.color.standard_background));
        }
    }

    public void dismissItemButton(View view){

        if(lastSelectedUID.equals("player") && selectedItemUIDfromPlayer != -1){
            dismissItem(lastSelectedUID, selectedItemUIDfromPlayer);
            selectedItemUIDfromPlayer = -1;
            playerGridView.invalidateViews();
            dismissView.setTextColor(getColor(R.color.standard_background));
        }
        else if(lastSelectedUID.equals("merchant") && selectedItemUIDFromMerch != -1){
            //dismissItem(lastSelectedUID, selectedItemUIDFromMerch);
            selectedItemUIDFromMerch = -1;
            //merchantGridView.invalidateViews();
            dismissView.setTextColor(getColor(R.color.standard_background));
            buyView.setTextColor(getColor(R.color.standard_background));
        }

        itemNameView.setText("");
        itemDesMainView.setText("");
        itemDesAddView.setText("");
        itemRarityView.setText("");
        itemPriceView.setText("");
    }



    /*

    Funktionen

     */



    private void showExpirationDate() {
        final SharedPreferences prefs = getSharedPreferences("TIME_TO_LEAVE_PREF", MODE_PRIVATE);
        long timeToShow, merchToLeaveDaytime = prefs.getLong("merchToLeaveDaytime", -1), merchChangeDate = prefs.getLong("merchChangeDate", -1);
        TextView merchantTimeView = (TextView) findViewById(R.id.activity_merchant_inventory_textView_time_to_next_merchant);

        if(merchToLeaveDaytime == -1) merchToLeaveDaytime = getNewMerchLeaveDaytime();
        if(merchChangeDate == -1) merchChangeDate = getNewMerchChangeDate();

        if(System.currentTimeMillis() >= merchChangeDate){
            timeToShow = (getNewMerchLeaveDaytime() - getNowInSeconds()) * 1000;
            prefs.edit().putLong("merchInventoryToLeaveDaytime", merchToLeaveDaytime);
            setNewItemMerchant();
            prefs.edit().putLong("merchInventoryChangeDate", getNewMerchChangeDate()).apply();
            prefs.edit().putLong("merchInventoryToLeaveDaytime", getNewMerchLeaveDaytime()).apply();

        }else{
            timeToShow = (merchToLeaveDaytime - getNowInSeconds()) * 1000;
        }

        merchantTimeView.setText(String.valueOf(timeToShow/1000/60));


            /*
    private void showExpirationDate() {
        final SharedPreferences prefs = getSharedPreferences("TIME_TO_LEAVE_PREF", MODE_PRIVATE);
        long timeToShow, merchToLeaveDaytime = prefs.getLong("merchInventoryToLeaveDaytime", -1), merchChangeDate = prefs.getLong("merchChangeDate", -1);

        if(merchToLeaveDaytime == -1) merchToLeaveDaytime = getNewMerchLeaveDaytime();
        if(merchChangeDate == -1) merchChangeDate = getNewMerchChangeDate();

        if(System.currentTimeMillis() >= merchChangeDate){
            timeToShow = (getNewMerchLeaveDaytime() - getNowInSeconds()) * 1000;
            prefs.edit().putLong("merchInventoryToLeaveDaytime", merchToLeaveDaytime);
            setNewItemMerchant();

        }else{
            timeToShow = (merchToLeaveDaytime - getNowInSeconds()) * 1000;
        }

        //currentMerchantId = prefs.getInt(MERCHANT_SLAVE_ID, 0);
        //merchantProfile.setImageResource(ImgRes.res(this, "merch", currentMerchantId + ""));

        final TextView merchantTimeView = (TextView) findViewById(R.id.activity_merchant_inventory_textView_time_to_next_merchant);

        new CountDownTimer(timeToShow, 1000 / 60) {

            public void onTick(long millisUntilFinished) {
                merchantTimeView.setText("" + millisUntilFinished / 1000 / 60);
                Log.v("MerchInventoryActivity", String.valueOf(millisUntilFinished/1000));
            }

            public void onFinish() {
                prefs.edit().putLong("merchInventoryChangeDate", getNewMerchChangeDate()).apply();
                prefs.edit().putLong("merchInventoryToLeaveDaytime", getNewMerchLeaveDaytime()).apply();

                setNewItemMerchant();
                //currentMerchantId = prefs.getInt(MERCHANT_SLAVE_ID, 0);
                //merchantProfile.setImageResource(ImgRes.res(getApplicationContext(), "merch", currentMerchantId + ""));

                showExpirationDate();
            }
        }.start();
    }
    */
    }

    private long getNowInSeconds(){
        return (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);
    }

    private long getNewMerchLeaveDaytime(){
        return 24*60*60;
    }

    private long getNewMerchChangeDate(){

        // Wenn jetzt nach Mittag, dann Mitternacht neuer Merchant, sonst zu Mittag
        long newFinishDate = getNewMerchLeaveDaytime();

        long todayInSeconds = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1) * 60 *60
                + Calendar.getInstance().get(Calendar.MINUTE) * 60
                + Calendar.getInstance().get(Calendar.SECOND);

        // neuer Abreise-Zeitpunkt des Händlers
        return System.currentTimeMillis() + (newFinishDate - todayInSeconds)*1000;
    }

    public void setItemShowcase(){
        if(lastSelectedUID.equals("player")){
            itemNameView.setText(String.valueOf(playerHelper.getItemName(selectedItemUIDfromPlayer)));
            itemDesMainView.setText(playerHelper.getItemDescriptionMain(selectedItemUIDfromPlayer));
            itemDesAddView.setText(playerHelper.getItemDescriptionAdditonal(selectedItemUIDfromPlayer) + "");
            itemRarityView.setText(String.valueOf(playerHelper.getItemRarity(selectedItemUIDfromPlayer)));
            itemPriceView.setText("");

        }else if(lastSelectedUID.equals("merchant")){
            itemNameView.setText(merchHelper.getItemName(selectedItemUIDFromMerch));
            itemDesMainView.setText(merchHelper.getItemDescriptionMain(selectedItemUIDFromMerch));
            itemDesAddView.setText(merchHelper.getItemDescriptionAdditonal(selectedItemUIDFromMerch));
            itemRarityView.setText(String.valueOf(merchHelper.getItemRarity(selectedItemUIDFromMerch)));
            itemPriceView.setText("$ " + merchHelper.getItemBuyCosts(selectedItemUIDFromMerch));
        }

        itemIconView.setImageResource(R.mipmap.ic_launcher);
    }

    public void merchantInventoryBackbuttonPressed(View view){
        super.onBackPressed();
        finish();
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

            if(validation < 0) Msg.msg(this, "ERROR @ insert @ setNewItemMerchant");
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

        if(validation < 0) Msg.msg(this, "ERROR @ addOneItemToPlayerDatabase");
    }

    private int getPosOfFreeSlotInPlayerItemDatabase(){

        ConstRes co = new ConstRes();
        int pos = -1;

        for(int i = 1; i <= playerHelper.getTaskCount(); i++){

            if(playerHelper.getItemName(i).equals(co.NOT_USED)){
                pos = i;
                i = (int) playerHelper.getTaskCount() + 1;

            }else if( i == playerHelper.getTaskCount() ){
                Msg.msg(this, "Alle Plätze bereits belegt");
            }
        }

        return pos;
    }

    private int getNumberOfUsedSlots(String id){
        int count = 0;

        switch (id){

            case "player":

                for(int i = 1; i <= playerHelper.getTaskCount(); i++){
                    if(! playerHelper.getItemName(i).equals(c.NOT_USED) ){
                        count++;
                    }
                }
                break;

            case "merchant":

                for(int i = 1; i <= merchHelper.getTaskCount(); i++){
                    if(! merchHelper.getItemName(i).equals(c.NOT_USED) ){
                        count++;
                    }
                }
                break;
        }

        return count;
    }



    /*

    Adapter

     */



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

            if( !playerHelper.getItemName(position + 1).equals(c.NOT_USED)) imageView.setImageResource(R.mipmap.ic_launcher);
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

            if( !merchHelper.getItemName(position+1).equals(c.NOT_USED)){
                imageView.setImageResource(R.mipmap.ic_launcher);
            }else{
                imageView.setImageResource(R.mipmap.ic_dot);
            }
            //imageView.setAdjustViewBounds(true) - bis zur Grenze der Gridansicht (?)
            return imageView;
        }
    }



    /*

    Funktionen zur Auslagerung von Initialisierngen

     */



    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void iniValues(){
        merchHelper = new DBmerchantItemsAdapter(this);
        playerHelper = new DBplayerItemsAdapter(this);

        playerGridView.setAdapter(new MyStuffAdapter(this, (int) playerHelper.getTaskCount()));
        merchantGridView.setAdapter(new MerchantAdapter(this));


        playerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                selectedItemUIDfromPlayer = position + 1;
                dismissView.setTextColor(Color.WHITE);
                lastSelectedUID = "player";
                setItemShowcase();
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
                setItemShowcase();
            }
        });

        fortuneView.setText("$" + prefs.getCurrentMoney(this, new ConstRes()));
        freeSlotsView.setText(getNumberOfUsedSlots("player") + "/" + playerHelper.getTaskCount() + " Plätzen belegt");
        showExpirationDate();
    }

    private void initializeViews(){
        hideSystemUI();
        playerGridView = (GridView) findViewById(R.id.inventory_gridView_my_stuff);
        merchantGridView = (GridView) findViewById(R.id.inventory_gridView_merchant);
        buyView = (TextView)findViewById(R.id.invetory_textView_buy);
        itemNameView = (TextView)findViewById(R.id.textView_merch_inv_item_name);
        itemDesMainView = (TextView)findViewById(R.id.textView_merch_inv_item_main_des);
        itemDesAddView = (TextView)findViewById(R.id.textView_merch_inv_item_add_des);
        itemPriceView = (TextView)findViewById(R.id.textView_merch_inv_item_price);
        itemRarityView = (TextView)findViewById(R.id.textView_merch_inv_item_rarity);
        itemIconView = (ImageView) findViewById(R.id.imageView_merch_inv_showcase_item);
        freeSlotsView = (TextView)findViewById(R.id.textView_merch_inv_free_slots);
        fortuneView = (TextView)findViewById(R.id.textView_merch_inv_fortune);
        dismissView = (TextView)findViewById(R.id.invetory_textView_dismiss);

        buyView.setTextColor(getColor(R.color.standard_background));
        dismissView.setTextColor(getColor(R.color.standard_background));
    }
}