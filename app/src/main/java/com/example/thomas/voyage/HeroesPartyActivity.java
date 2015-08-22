package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HeroesPartyActivity extends Activity {

    private RelativeLayout dataContainerLayout;
    private TextView nameView, classesView, costsView, textView_slots, hintQuestView, hintDismissHeroView, dismissView, questView, buyView;
    private DBheroesAdapter heroesHelper;
    private ListView listview;
    private long slotsInHeroesDatabase = 0;
    private int selectedHeroIdFromDatabase = -1;
    private String origin = "";
    private repoConstants co = new repoConstants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_party);
        heroesHelper = new DBheroesAdapter(this);

        hideSystemUI();
        initializeViews();
        textView_slots.setText(getUsedSlotsInHeroesDatabase() + " / " + slotsInHeroesDatabase);

        Bundle b = getIntent().getExtras();
        if(b != null){
            origin = b.getString(co.ORIGIN, "StartActivity");
        }

        //listview = (ListView) findViewById(R.id.activity_heroes_party_listView);
        String[] values = new String[co.HEROES_IN_LISTVIEW];
        for (int i = 1; i <= co.HEROES_IN_LISTVIEW; i++) {
            values[i - 1] = i + "";
        }

        final ArrayList<String> list = new ArrayList<>();
        /*
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }*/

        for (String i : values ) {
            list.add(i);
        }

        if (heroesHelper.equals(null)) {
            Message.message(this, "No HeroesDatabase set yet created - ya betta' do!");

        } else {

            final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, values);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    selectedHeroIdFromDatabase = position + 1;
                    questView.setBackgroundColor(getResources().getColor(R.color.active_field));
                    dismissView.setBackgroundColor(getResources().getColor(R.color.active_field));
                    dataContainerLayout.setVisibility(View.VISIBLE);

                    if(heroesHelper.getHeroName(selectedHeroIdFromDatabase).equals(co.NOT_USED)){

                        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
                        startActivity(i);

                    }else{

                        nameView.setText(heroesHelper.getHeroName(selectedHeroIdFromDatabase));
                        classesView.setText(heroesHelper.getHeroPrimaryClass(selectedHeroIdFromDatabase) + '\n' + heroesHelper.getHeroSecondaryClass(selectedHeroIdFromDatabase));
                        costsView.setText(Integer.toString(heroesHelper.getHeroCosts(selectedHeroIdFromDatabase))
                                + '\n' + heroesHelper.getHeroImgRes(selectedHeroIdFromDatabase));
                    }


                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
        listview.invalidateViews();
    }

    private long getUsedSlotsInHeroesDatabase() {
        long countUsed = 0;
        slotsInHeroesDatabase = heroesHelper.getTaskCount();

        for (long i = 0; i < slotsInHeroesDatabase; i++) {
            if (!(heroesHelper.getHeroName(i + 1).equals(co.NOT_USED))) {
                countUsed++;
            }
        }

        if(countUsed == slotsInHeroesDatabase) buyView.setBackgroundColor(getResources().getColor(R.color.inactive_field));
        else buyView.setBackgroundColor(getResources().getColor(R.color.active_field));

        return countUsed;
    }

    public void heroesPartyDismissHero(View view){

        if( (selectedHeroIdFromDatabase != -1) && (!heroesHelper.getHeroName(selectedHeroIdFromDatabase).equals(co.NOT_USED)) ){

            heroesHelper.markOneRowAsUnused(selectedHeroIdFromDatabase);
            listview.invalidateViews();
            textView_slots.setText(getUsedSlotsInHeroesDatabase() + " / " + slotsInHeroesDatabase);

            dataContainerLayout.setVisibility(View.INVISIBLE);
            selectedHeroIdFromDatabase = -1;
            dismissView.setBackgroundColor(getResources().getColor(R.color.inactive_field));

            questView.setBackgroundColor(getResources().getColor(R.color.inactive_field));

        }else{
            showHint("dismissHero");
        }
    }

    public void buyHeroFromMerchant(View view){
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
        finish();
    }

    public void commitToQuest(View view){

        Intent i;

        if(selectedHeroIdFromDatabase != -1){

            switch (origin){
                case "PrepareCombatActivity":
                    i = new Intent(getApplicationContext(), PrepareCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                case "WorldMapQuickCombatActivity":
                    i = new Intent(getApplicationContext(), WorldMapQuickCombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;

                default:
                    i = new Intent(getApplicationContext(), CombatActivity.class);
                    passHeroesParameterstoNewActivity(i);
                    startActivity(i);
                    finish();
                    break;
            }

        }else{
            showHint("commitToQuest");
        }

/*
        if(heroesHelper.getTaskCount() > 0 && selectedHeroIdFromDatabase > 0){
            selectedHeroIdFromDatabase = 1;
            passHeroesParameterstoNewActivity(i);
        }else {
            Message.message(this, "No hero selected");
        }*/
    }

    public void showHint(final String id){
        hintQuestView = (TextView) findViewById(R.id.heroes_party_hint_commit_to_quest);
        hintDismissHeroView = (TextView)findViewById(R.id.heroes_party_hint_dismiss_hero);
        final View dummyView;

        if(id.equals("commitToQuest")){
            dummyView = hintQuestView;
        }else {
            dummyView = hintDismissHeroView;
        }

        dummyView.setVisibility(View.VISIBLE);

        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {dummyView.setVisibility(View.INVISIBLE);}
        }.start();


    }

    public void passHeroesParameterstoNewActivity(Intent i){

        if(selectedHeroIdFromDatabase != -1){

             /*

             Um die Funktion nutzen zu können, muss ein Held ausgewählt sein -> für Kämpfe gedacht

            */

            i.putExtra(co.HEROES_NAME, heroesHelper.getHeroName(selectedHeroIdFromDatabase));
            i.putExtra(co.HEROES_PRIMARY_CLASS, heroesHelper.getHeroPrimaryClass(selectedHeroIdFromDatabase));
            i.putExtra(co.HEROES_SECONDARY_CLASS,heroesHelper.getHeroSecondaryClass(selectedHeroIdFromDatabase));
            i.putExtra(co.HEROES_HITPOINTS,heroesHelper.getHeroHitpoints(selectedHeroIdFromDatabase));
            i.putExtra(co.HEROES_COSTS, heroesHelper.getHeroCosts(selectedHeroIdFromDatabase));
            i.putExtra(co.IMAGE_RESOURCE, heroesHelper.getHeroImgRes(selectedHeroIdFromDatabase));
        }

        i.putExtra(co.ORIGIN, "HeroesPartyActivity");
    }

    public void heroesPartyBackbuttonPressed(View view) {
        onBackPressed();
        finish();
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void initializeViews() {

        nameView = (TextView) findViewById(R.id.textView_party_name);
        classesView = (TextView) findViewById(R.id.textView_party_classes);
        costsView = (TextView) findViewById(R.id.textView_party_costs);
        textView_slots = (TextView) findViewById(R.id.hero_size_display);
        listview = (ListView) findViewById(R.id.activity_heroes_party_listView);
        dismissView = (TextView) findViewById(R.id.hero_dismiss);
        questView = (TextView) findViewById(R.id.hero_set_as_adventurer);
        buyView = (TextView) findViewById(R.id.heroes_party_buy_hero_from_merchant);
        dataContainerLayout = (RelativeLayout) findViewById(R.id.heroes_party_realative_layout);
    }

    class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        DBheroesAdapter heroesHelper;

        public MySimpleArrayAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;

            heroesHelper = new DBheroesAdapter(this.context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.listview_heroes_rowlayout, parent, false);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView_rowlayout);

            if ( !heroesHelper.getHeroName(position + 1).equals(co.NOT_USED) ) {
                imageView.setImageResource(ImgRes.res(context, "hero", heroesHelper.getHeroImgRes(position + 1)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            return rowView;
        }
    }

}



