package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatCricket extends Activity {

    int count = 21;
    int[] array;
    private static GridView cricketView;
    private static List<Integer> markedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_cricket);
        hideSystemUI();

        array  = new int[count];
        for(int i = 0, temp = 0; i < count; i++){

            if(i == 20) temp = 25;
            else temp = i+1;

            array[i] = temp;
        }

        cricketView = (GridView) findViewById(R.id.cricket_gridview);
        cricketView.setAdapter(new SimpleNumberAdapter(this));
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                markedList.add(position);
                cricketView.invalidateViews();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }








    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            TextView numView, playerOneView, playerTwoView;
            Space spaceOne, spaceTwo;
            int progressPlayerOne = 0, progressPlayerTwo = 0;

            ViewHolder(View v){
                numView = (TextView) v.findViewById(R.id.quick_cricket_card_textview);
                playerOneView = (TextView) v.findViewById(R.id.cricketcard_progress_1);
                playerTwoView = (TextView) v.findViewById(R.id.cricketcard_progress_2);
                spaceOne = (Space) v.findViewById(R.id.cricketcard_space_1);
                spaceTwo = (Space) v.findViewById(R.id.cricketcard_space_2);
            }
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.quick_cricket_card, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(markedList.contains(position)){
                //holder.numView.setText(Integer.toString(array[position]));
                convertView.setBackgroundColor(Color.BLACK);

            }else{
                convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners));
            }

            holder.numView.setText(array[position] + "");

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
