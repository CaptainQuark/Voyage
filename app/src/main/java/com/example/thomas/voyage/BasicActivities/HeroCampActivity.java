package com.example.thomas.voyage.BasicActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class HeroCampActivity extends Activity {

    private GridView heroImagesGridView;
    private DBheroesAdapter heroesHelper;
    private TextView slotsView;
    private List<Hero> heroesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_camp);
        hideSystemUI();
        ConstRes c = new ConstRes();

        heroesHelper = new DBheroesAdapter(this);
        heroesList = new ArrayList<>();

        for(int i = 1; i <= heroesHelper.getTaskCount(); i++){

            if(!heroesHelper.getHeroName(i).equals(c.NOT_USED)){
                String name = heroesHelper.getHeroName(i);
                String prim = heroesHelper.getHeroPrimaryClass(i);
                String sec = heroesHelper.getHeroSecondaryClass(i);
                String imgRes = heroesHelper.getHeroImgRes(i);
                int hp = heroesHelper.getHeroHitpoints(i);
                int costs = heroesHelper.getHeroCosts(i);

                heroesList.add( new Hero(name, prim, sec, imgRes, hp, costs));
            }
        }

        heroImagesGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroImagesGridView.setAdapter(new HeroImagesAdapter(this));
        heroImagesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Msg.msg(getApplicationContext(), "Hero tapped");
            }
        });

        slotsView = (TextView) findViewById(R.id.textview_camp_slots);
        setSlotsView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        heroImagesGridView.invalidate();
        setSlotsView();
        hideSystemUI();
    }

    public void heroesCampBackButton(View v){
        super.onBackPressed();
        finish();
    }

    public void campBuyNewHero(View v){
        Intent i = new Intent(this, MerchantHeroActivity.class);
        startActivity(i);
    }

    public void commitToQuest(View v){
        Msg.msg(this, "Not yet implemented");
    }

    public void dismissHero(View v){
        Msg.msg(this, "Not yet implemented");
    }

    private void setSlotsView(){
        slotsView.setText(heroesList.size() + " / " + heroesHelper.getTaskCount());
    }


















    public class HeroImagesAdapter extends BaseAdapter {
        private Context mContext;

        public HeroImagesAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return heroesList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {

            private TextView nameView,classesView, hpView, costsView, battlesView;
            private ImageView profileView;

            ViewHolder(View v) {
                nameView = (TextView) v.findViewById(R.id.textView_camp_card_name);
                classesView = (TextView) v.findViewById(R.id.textView_camp_card_prim_and_sec);
                hpView = (TextView) v.findViewById(R.id.textView_camp_card_hp);
                costsView = (TextView) v.findViewById(R.id.textView_camp_card_costs);
                battlesView = (TextView) v.findViewById(R.id.textView_camp_card_battles);
                profileView = (ImageView) v.findViewById(R.id.imageView_camp_card_profile);
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.hero_camp_card, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.profileView.setImageResource(getApplicationContext().getResources().getIdentifier(heroesList.get(position).getStrings("imageResource"), "mipmap", getPackageName()));
            holder.nameView.setText(heroesHelper.getHeroName(position + 1) + "");
            holder.classesView.setText(heroesList.get(position).getClassPrimary() + " und " + heroesList.get(position).getClassSecondary());
            holder.hpView.setText(heroesList.get(position).getHp() + " / " + heroesList.get(position).getHpConst());
            holder.costsView.setText(heroesList.get(position).getCosts() + "");
            holder.battlesView.setText("0");

            /*
            if (markedList.contains(position)) {
                //holder.numView.setText(Integer.toString(valuesArray[position]));
                convertView.setBackgroundColor(Color.BLACK);

            } else {
                convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners));
            }
            */

            return convertView;
        }
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

}
