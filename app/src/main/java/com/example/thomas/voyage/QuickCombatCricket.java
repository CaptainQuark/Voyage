package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class QuickCombatCricket extends Activity {

    int numCards = 21, activePlayer = 0, numPlayers = 2, tempNumThrows = 0;
    final int totalNumThrowsPerPlayer = 3;
    int[] array;
    private GridView cricketView;
    private List<Integer> markedList = new ArrayList<>(), scoreList = new ArrayList<>();
    private List<CardData> cardDataList = new ArrayList<>();
    private TextView playerNameOneView, playerNameTwoView, playerScoreOneView, playerScoreTwoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_cricket);
        hideSystemUI();

        playerScoreOneView = (TextView) findViewById(R.id.cricket_score_player_1);
        playerScoreTwoView = (TextView) findViewById(R.id.cricket_score_player_2);


        for(int i = 0; i < numPlayers; i++){
            scoreList.add(0);
        }

        array  = new int[numCards];
        for(int i = 0, temp = 0; i < numCards; i++){

            if(i == 20) temp = 25;
            else temp = i+1;

            array[i] = temp;

            cardDataList.add( new CardData(i) );
        }

        cricketView = (GridView) findViewById(R.id.cricket_gridview);
        cricketView.setAdapter(new SimpleNumberAdapter(this));
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //markedList.add(position);

                if( tempNumThrows < totalNumThrowsPerPlayer ){
                    tempNumThrows++;
                    Message.message(getApplicationContext(), tempNumThrows + "");
                }else {
                    tempNumThrows = 1;

                    if(activePlayer == 0) activePlayer = 1;
                    else activePlayer = 0;
                    Message.message(getApplicationContext(), activePlayer + " = activePlayer");
            }

                cardDataList.get(position).progressPlayers.set(activePlayer,
                        cardDataList.get(position).progressPlayers.get(activePlayer) + 0.33f);

                cricketView.invalidateViews();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    private class CardData{
        int pos;
        List<Float> progressPlayers;
        boolean isClosed;

        public CardData(int position){
            progressPlayers = new ArrayList<>();

            for(int i = 0; i < numPlayers; i++){
                progressPlayers.add(0f);
            }

            pos = position;
            isClosed = false;
        }
    }










    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return numCards;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            TextView numView;
            ImageView playerOneView, playerTwoView;
            Space spaceOne, spaceTwo;
            LinearLayout.LayoutParams paramsProgressOne, paramsProgressTwo, paramsSpaceOne, paramsSpaceTwo;

            ViewHolder(View v){
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
        }

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

            holder.numView.setText(array[position] + "");
            //holder.numView.setText( (cardDataList.get(position).progressPlayers.get(activePlayer)) + "" );

            if(markedList.contains(position)){
                //holder.numView.setText(Integer.toString(array[position]));
                convertView.setBackgroundColor(Color.BLACK);

            }else{
                convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners));
            }

            holder.paramsProgressOne.weight = cardDataList.get(position).progressPlayers.get(0);
            holder.paramsProgressTwo.weight = cardDataList.get(position).progressPlayers.get(1);
            holder.paramsSpaceOne.weight = ( 1 - cardDataList.get(position).progressPlayers.get(0) );
            holder.paramsSpaceTwo.weight = ( 1 - cardDataList.get(position).progressPlayers.get(1) );

            holder.playerOneView.setLayoutParams(holder.paramsProgressOne);
            holder.playerTwoView.setLayoutParams(holder.paramsProgressTwo);
            holder.spaceOne.setLayoutParams(holder.paramsSpaceOne);
            holder.spaceTwo.setLayoutParams(holder.paramsSpaceTwo);

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
