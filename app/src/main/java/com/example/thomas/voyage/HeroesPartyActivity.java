package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HeroesPartyActivity extends Activity {

    private final int HEROES_IN_LISTVIEW = 10;
    private TextView nameView, classesView, costsView, textView_slots;
    private DBheroesAdapter heroesHelper;
    private ListView listview;
    private long slotsInHeroesDatabase = 0;
    private int selectedHeroIdFromDatabase = -1;
    private String origin = "";

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
            origin = b.getString("ORIGIN", "StartActivity");
        }

        //listview = (ListView) findViewById(R.id.activity_heroes_party_listView);
        String[] values = new String[HEROES_IN_LISTVIEW];
        for (int i = 1; i <= HEROES_IN_LISTVIEW; i++) {
            values[i - 1] = i + "";
        }

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
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

                    if(heroesHelper.getHeroName(selectedHeroIdFromDatabase).equals(getResources().getString(R.string.indicator_unused_row))){

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

    private long getUsedSlotsInHeroesDatabase() {
        long countUsed = 0;
        slotsInHeroesDatabase = heroesHelper.getTaskCount();

        for (long i = 0; i < slotsInHeroesDatabase; i++) {
            if (!(heroesHelper.getHeroName(i + 1).equals(getResources().getString(R.string.indicator_unused_row)))) {
                countUsed++;
            }
        }

        return countUsed;
    }

    public void heroesPartyDismissHero(View view){

        if(heroesHelper.getHeroName(selectedHeroIdFromDatabase) != "NOT_USED"){
            heroesHelper.markOneRowAsUnused(selectedHeroIdFromDatabase);
            listview.invalidateViews();
            textView_slots.setText(getUsedSlotsInHeroesDatabase() + " / " + slotsInHeroesDatabase);
        }
    }

    public void buyHeroFromMerchant(View view){
        Intent i = new Intent(getApplicationContext(), MerchantHeroActivity.class);
        startActivity(i);
        finish();
    }

    public void commitToQuest(View view){

        Intent i;

        switch (origin){
            case "CombatActivity":
                i = new Intent(getApplicationContext(), CombatActivity.class);
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
                i = new Intent(getApplicationContext(), CombatWhiteActivity.class);
                passHeroesParameterstoNewActivity(i);
                startActivity(i);
                finish();
                break;
        }

        if(heroesHelper.getTaskCount() > 0 && selectedHeroIdFromDatabase > 0){
            selectedHeroIdFromDatabase = 1;
            passHeroesParameterstoNewActivity(i);
        }else {
            Message.message(this, "No hero selected");
        }
    }

    public void passHeroesParameterstoNewActivity(Intent i){

        if(selectedHeroIdFromDatabase != -1){

             /*

             Um die Funktion nutzen zu können, muss ein Held ausgewählt sein -> für Kämpfe gedacht

            */

            i.putExtra("HEROES_NAME", heroesHelper.getHeroName(selectedHeroIdFromDatabase));
            i.putExtra("HEROES_PRIMARY_CLASS", heroesHelper.getHeroPrimaryClass(selectedHeroIdFromDatabase));
            i.putExtra("HEROES_SECONDARY_CLASS",heroesHelper.getHeroSecondaryClass(selectedHeroIdFromDatabase));
            i.putExtra("HEROES_HITPOINTS",heroesHelper.getHeroHitpoints(selectedHeroIdFromDatabase));
            i.putExtra("HEROES_COSTS", heroesHelper.getHeroCosts(selectedHeroIdFromDatabase));
            i.putExtra("IMAGE_RESOURCE", heroesHelper.getHeroImgRes(selectedHeroIdFromDatabase));
        }

        i.putExtra("ORIGIN", "HeroesPartyActivity");
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

            if ( !heroesHelper.getHeroName(position + 1).equals(context.getString(R.string.indicator_unused_row)) ) {
                imageView.setImageResource(ImgRes.res(context, "hero", heroesHelper.getHeroImgRes(position + 1)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            return rowView;
        }
    }
}



