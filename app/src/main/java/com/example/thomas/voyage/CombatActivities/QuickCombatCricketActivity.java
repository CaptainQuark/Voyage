package com.example.thomas.voyage.CombatActivities;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatCricketActivity extends Activity {

    private int activePlayer = 0, numPlayers = 2, tempNumThrows = 0, maxNumberOfNeededHits = 0;
    private float oneFractionOfTotalHitsNeeded = 0;
    final int totalNumThrowsPerPlayer = 3;
    int[] valuesArray;
    private GridView cricketView;
    private List<Integer> markedList = new ArrayList<>(), scoreList = new ArrayList<>(), numOfAchievedNeededHitsList;
    private List<CardData> cardDataList = new ArrayList<>();
    private TextView playerNameOneView, playerNameTwoView, playerScoreOneView, playerScoreTwoView, throwCountView;
    private ImageView indicationBarOne, indicationBarTwo;
    private LinearLayout.LayoutParams paramsIndiBarOne, paramsIndiBarTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_cricket);
        hideSystemUI();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            valuesArray = b.getIntArray("LIST_OF_SELECTED_VALUES");
        }else{
            valuesArray = new int[1];
        }

        maxNumberOfNeededHits = valuesArray.length;
        oneFractionOfTotalHitsNeeded = (float) (1 / maxNumberOfNeededHits);
        numOfAchievedNeededHitsList = new ArrayList<>();

        numOfAchievedNeededHitsList.add(0);
        numOfAchievedNeededHitsList.add(0);
        //Message.message(getApplication(), "numOfAchievedNeededHitsList: " + numOfAchievedNeededHitsList.size());


        playerScoreOneView = (TextView) findViewById(R.id.cricket_score_player_1);
        playerScoreTwoView = (TextView) findViewById(R.id.cricket_score_player_2);
        throwCountView = (TextView) findViewById(R.id.cricket_throw_count);
        indicationBarOne = (ImageView)findViewById(R.id.cricket_indication_bar_1);
        indicationBarTwo = (ImageView) findViewById(R.id.cricket_indication_bar_2);
        paramsIndiBarOne = (LinearLayout.LayoutParams) indicationBarOne.getLayoutParams();
        paramsIndiBarTwo = (LinearLayout.LayoutParams) indicationBarTwo.getLayoutParams();

        for(int i = 0; i < numPlayers; i++){
            scoreList.add(0);
        }

        for(int i = 0; i < valuesArray.length; i++){
            cardDataList.add( new CardData(i) );
        }

        cricketView = (GridView) findViewById(R.id.cricket_gridview);
        cricketView.setAdapter(new SimpleNumberAdapter(this));
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                handleThrow(position);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void quickCricketBackButton(View view){
        super.onBackPressed();
        finish();
    }

    public void onCricketUndo(View view){
        Message.message(this, "WA'SUP!!!\n...no undo yet implemented...");
    }

    public void quickCombatMissButton(View view){

        if( tempNumThrows < totalNumThrowsPerPlayer ){
            tempNumThrows++;
            //Message.message(getApplicationContext(), tempNumThrows + "");
        }else {
            tempNumThrows = 1;

            if(activePlayer == 0) activePlayer = 1;
            else activePlayer = 0;
            //Message.message(getApplicationContext(), activePlayer + " = activePlayer");
        }

        throwCountView.setText(tempNumThrows + ".");
    }

    private void handleThrow(int position){
        //markedList.add(position);

        if(cardDataList.get(position).isClosed)
        {
            Message.message(getApplicationContext(), "Diese Zahl ist bereits geschlossen");
        }
        else
        {

            if( tempNumThrows < totalNumThrowsPerPlayer ){
                tempNumThrows++;
                //Message.message(getApplicationContext(), tempNumThrows + "");
            }else {
                tempNumThrows = 1;

                if(activePlayer == 0) activePlayer = 1;
                else activePlayer = 0;
                //Message.message(getApplicationContext(), activePlayer + " = activePlayer");
            }

            throwCountView.setText( ((tempNumThrows%3)+1) + ".");

            // Indication-Bar-Berechnungen
            try{
                numOfAchievedNeededHitsList.set(activePlayer, numOfAchievedNeededHitsList.get(activePlayer) + 1);

                List<Float> diffList = new ArrayList<>();

                diffList.add( numOfAchievedNeededHitsList.get(0) * oneFractionOfTotalHitsNeeded );
                diffList.add( numOfAchievedNeededHitsList.get(1) * oneFractionOfTotalHitsNeeded );

                //float diff = ( numOfAchievedNeededHitsList.get(activePlayer) * oneFractionOfTotalHitsNeeded);

                float diffCorrection = diffList.get(0) - diffList.get(1);
                if( diffCorrection < 0){
                    diffCorrection = diffCorrection * (-1);
                }

                diffCorrection = diffCorrection / 2;


                diffList.set(0, diffList.get(0) + diffCorrection);
                diffList.set(1, diffList.get(1) + diffCorrection);


                paramsIndiBarOne.weight = diffList.get(0);
                paramsIndiBarTwo.weight = diffList.get(1);
                indicationBarOne.setLayoutParams(paramsIndiBarOne);
                indicationBarTwo.setLayoutParams(paramsIndiBarTwo);

            }catch (NullPointerException e){

                Message.message(getApplicationContext(), e + "");
            }


            // Fortschritt für konkrete Ziel-Zahl berechnen
            cardDataList.get(position).progressPlayers.set(activePlayer,
                    cardDataList.get(position).progressPlayers.get(activePlayer) + 0.33f);

            // Überprüfen, ob konkrete Ziel-Zahl jetzt geschlossen werden soll
            int validateIsClosed = 0;
            for( int i = 0; i < numPlayers; i++){
                if( (cardDataList.get(position).progressPlayers.get(i) >= 0.99f) ) validateIsClosed++;
                //Message.message(getApplicationContext(), "i: " + i + ", validateIsClosed: " + validateIsClosed);
            }
            if(validateIsClosed == numPlayers){
                cardDataList.get(position).isClosed = true;
            }
            else if( cardDataList.get(position).progressPlayers.get(activePlayer) > 0.99f){

                scoreList.set(activePlayer, scoreList.get(activePlayer) + valuesArray[position]);
                if(activePlayer == 0) playerScoreOneView.setText(scoreList.get(activePlayer) + "");
                if(activePlayer == 1) playerScoreTwoView.setText(scoreList.get(activePlayer) + "");
            }

            // GridView-Ansicht aktualisieren, ob die obigen Berechngen und Veränderungen sichtbar zu machen
            cricketView.invalidateViews();

            // Auf Siegesbedingungen überprüfen
            for(int playerNum = 0; playerNum < numPlayers; playerNum++){

                for(int i = 0, numToWin = 0; i < valuesArray.length; i++){

                    if(scoreList.get(playerNum) >= scoreList.get((playerNum+1) % numPlayers)){

                        if( cardDataList.get(i).progressPlayers.get(playerNum) >= 0.99f && cardDataList.get(i).progressPlayers.get((playerNum+1) % numPlayers) < 0.99f ){
                            numToWin++;
                        }else if( cardDataList.get(i).isClosed){
                            numToWin++;
                        }

                        if( numToWin == valuesArray.length ){
                            Message.message(getApplicationContext(), "Spieler + " + (playerNum+1) + " hat gewonnen!");
                        }
                    }
                }
            }
        }
    }

    private class CardData{
        int pos;
        List<Float> progressPlayers;
        boolean isClosed = false;

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
            return valuesArray.length;
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

            ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.quick_cricket_card, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.numView.setText(valuesArray[position] + "");
            //holder.numView.setText( (cardDataList.get(position).progressPlayers.get(activePlayer)) + "" );

            if(markedList.contains(position)){
                //holder.numView.setText(Integer.toString(valuesArray[position]));
                convertView.setBackgroundColor(Color.BLACK);

            }else{
                convertView.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners));
            }

            if(cardDataList.get(position).isClosed){

                holder.paramsSpaceOne.weight = ( 0 );
                holder.paramsSpaceTwo.weight = ( 0 );
                holder.spaceOne.setLayoutParams(holder.paramsSpaceOne);
                holder.spaceTwo.setLayoutParams(holder.paramsSpaceTwo);

                convertView.setBackground(getResources().getDrawable(R.drawable.rounded_corners_black));
                holder.playerOneView.setBackground(getResources().getDrawable(R.drawable.rounded_corners_black));
                holder.playerTwoView.setBackground(getResources().getDrawable(R.drawable.rounded_corners_black));

            }else{
                holder.paramsProgressOne.weight = cardDataList.get(position).progressPlayers.get(0);
                holder.paramsProgressTwo.weight = cardDataList.get(position).progressPlayers.get(1);
                holder.paramsSpaceOne.weight = ( 1 - cardDataList.get(position).progressPlayers.get(0) );
                holder.paramsSpaceTwo.weight = ( 1 - cardDataList.get(position).progressPlayers.get(1) );

                holder.playerOneView.setLayoutParams(holder.paramsProgressOne);
                holder.playerTwoView.setLayoutParams(holder.paramsProgressTwo);
                holder.spaceOne.setLayoutParams(holder.paramsSpaceOne);
                holder.spaceTwo.setLayoutParams(holder.paramsSpaceTwo);
            }



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
