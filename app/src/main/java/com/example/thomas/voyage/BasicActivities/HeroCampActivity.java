package com.example.thomas.voyage.BasicActivities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class HeroCampActivity extends Activity {

    private GridView heroImagesGridView;
    private DBheroesAdapter heroesHelper;
    private List<Hero> heroesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_camp);
        hideSystemUI();

        heroesHelper = new DBheroesAdapter(this);
        heroesList = new ArrayList<>();

        for(int i = 1; i <= heroesHelper.getTaskCount(); i++){
            String name = heroesHelper.getHeroName(i);
            String prim = heroesHelper.getHeroPrimaryClass(i);
            String sec = heroesHelper.getHeroSecondaryClass(i);
            String imgRes = heroesHelper.getHeroImgRes(i);
            int hp = heroesHelper.getHeroHitpoints(i);
            int costs = heroesHelper.getHeroCosts(i);

            heroesList.add( new Hero(name, prim, sec, imgRes, hp, costs));
        }

        heroImagesGridView = (GridView) findViewById(R.id.heroes_camp_gridview);
        heroImagesGridView.setAdapter(new HeroImagesAdapter(this));
        heroImagesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Message.message(getApplicationContext(), "Hero tapped");
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
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
            /*
            TextView numView;
            ImageView playerOneView, playerTwoView;
            Space spaceOne, spaceTwo;
            LinearLayout.LayoutParams paramsProgressOne, paramsProgressTwo, paramsSpaceOne, paramsSpaceTwo;

            ViewHolder(View v) {
                numView = (TextView) v.findViewById(R.id.quick_cricket_card_textview);
                playerOneView = (ImageView) v.findViewById(R.id.cricketcard_progress_1);
                playerTwoView = (ImageView) v.findViewById(R.id.cricketcard_progress_2);
                spaceOne = (Space) v.findViewById(R.id.cricketcard_space_1);
                spaceTwo = (Space) v.findViewById(R.id.cricketcard_space_2);

                paramsProgressOne = (LinearLayout.LayoutParams) playerOneView.getLayoutParams();
                paramsProgressTwo = (LinearLayout.LayoutParams) playerTwoView.getLayoutParams();

                paramsSpaceOne = (LinearLayout.LayoutParams) spaceOne.getLayoutParams();
                paramsSpaceTwo = (LinearLayout.LayoutParams) spaceTwo.getLayoutParams();

            }
            */

            ViewHolder(View v){

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

            //holder.numView.setText(valuesArray[position] + "");
            //holder.numView.setText( (cardDataList.get(position).progressPlayers.get(activePlayer)) + "" );

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
