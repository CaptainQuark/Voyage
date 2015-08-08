package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CombatActivity extends Activity {

    static int monsterHealth = 500;
    static int scoreMultiplier = 1, scoreMultiplier1 = 1,scoreMultiplier2 = 1,scoreMultiplier3 = 1;
    static String finishMultiplier = "UniversalSingle", FinishMultiplier1 = "UniversalSingle",finishMultiplier2 = "UniversalSingle",finishMultiplier3 = "UniversalSingle";
    static int scoreField1 = 0,scoreField2 = 0,scoreField3 = 0,  dartCount = 1;
    static TextView monsterHealthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        hideSystemUI();

        int[] numbersOfBoardList = new int[20];
        for (int i = 0; i < 20; i++) {
            numbersOfBoardList[i] = i + 1;
        }

        int[] specialSymbolsList = new int[10];
        for (int i = 0; i < 10; i++){
            specialSymbolsList[i] = i+1 ;
        }
        monsterHealthView = (TextView) findViewById(R.id.monsterHealthView);
        GridView gridViewNumnbers = (GridView) findViewById(R.id.activity_combat_gridView_numbers);
        GridView gridViewSpecials = (GridView) findViewById(R.id.activity_combat_gridView_specials);
        gridViewNumnbers.setAdapter(new CustomAdapter(this, numbersOfBoardList));
        gridViewSpecials.setAdapter(new CustomAdapter(this, specialSymbolsList));
    }

    public void activityCombatBackToMain(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
    }

    private static class CustomAdapter extends BaseAdapter {

        int[] result;
        int currentSelectedPosition = 0, lastSelectedPosition = 0;
        Context context;
        private LayoutInflater inflater = null;

        public CustomAdapter(CombatActivity combatActivity, int[] prgmNameList) {
            result = prgmNameList;
            context = combatActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {

            final Holder holder = new Holder();
            View rowView;

            if(result.length == 20){
                rowView = inflater.inflate(R.layout.gridview_combat_left_panel, null);
                holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);
                holder.tv.setText(result[position] + "");
            }else{
                rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    lastSelectedPosition = currentSelectedPosition;
                    currentSelectedPosition = position;

                    //holder.tv.setBackgroundColor(context.getResources().getColor(R.color.highlight_cherryred));

                    if (dartCount == 1) {
                        scoreMultiplier1 = scoreMultiplier;
                        scoreField1 = result[position];
                        dartCount++;
                    } else if (dartCount == 2) {
                        scoreMultiplier2 = scoreMultiplier;
                        scoreField2 = result[position];
                        dartCount++;
                    } else if (dartCount == 3) {
                        scoreMultiplier3 = scoreMultiplier;
                        scoreField3 = result[position];
                        dartCount = 1;
                        monsterHealth -= scoreMultiplier1 * scoreField1 + scoreMultiplier2 * scoreField2 + scoreMultiplier3 * scoreField3;
                    }

                    monsterHealthView.setText(monsterHealth + "");

                }
            });

            return rowView;
        }
        public class Holder {
            TextView tv;
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







// CODE 2 LEARN
            /*if ((((result[position] - posCorrectionModFive) % (5 * (posCorrectionModFive + 1)) == 0))) {

                posCorrectionModFive++;
                rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                holder.tv = (ToggleButton) rowView.findViewById(R.id.gridView_combat_right_panel_textView);

            } else if ((((result[position]) % 6) == 0)) {

                rowView = inflater.inflate(R.layout.gridview_combat_right_panel, null);
                holder.tv = (ToggleButton) rowView.findViewById(R.id.gridView_combat_right_panel_textView);

            }else{

                rowView = inflater.inflate(R.layout.gridview_combat, null);
                holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);
                holder.tv.setText(result[position] + "");

            }*/



