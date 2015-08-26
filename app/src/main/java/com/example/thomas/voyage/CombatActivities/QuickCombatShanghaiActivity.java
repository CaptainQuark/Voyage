package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.games.Player;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatShanghaiActivity extends Activity {

    private int activePlayer = 0, roundCount = 0, throwCount = 1, numThrowsPerPlayerPerRound = 3;
    private int[] valArray = {1,2,3,4,5,6,7,8,9}, shanghaiArray = {0,0,0};
    private List<PlayerDataHolder> playerDataList;
    private TextView throwCountView, roundCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_shanghai);
        hideSystemUI();
        iniViews();
        iniVals(this);

        playerDataList = new ArrayList<>();
        for(int i = 0; i < 2; i++) playerDataList.add( new PlayerDataHolder(this, (i+1)) );
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void onShanghaiScoreField(View view){
        int scoreMulti = -1;

        switch (view.getId()){

            case R.id.shanghai_score_field_3_pl_1:
                scoreMulti = 3;
                break;
            case R.id.shanghai_score_field_2_pl_1:
                scoreMulti = 2;
                break;
            case R.id.shanghai_score_field_1_pl_1:
                scoreMulti = 1;
                break;
            case R.id.shanghai_score_field_3_pl_2:
                scoreMulti = 3;
                break;
            case R.id.shanghai_score_field_2_pl_2:
                scoreMulti = 2;
                break;
            case R.id.shanghai_score_field_1_pl_2:
                scoreMulti = 1;
                break;
            default:
                Message.message(this, "DEFAULT @ 'onShanghaiScoreField'");
                break;
        }

        playerDataList.get(activePlayer).addNewScoreValue(valArray[roundCount] * scoreMulti);
        throwCount++;

        if(throwCount <= numThrowsPerPlayerPerRound){

            shanghaiArray[roundCount-1] = scoreMulti;

            if(throwCountView == roundCountView){
                if(checkForShanghai(1) && checkForShanghai(2) && checkForShanghai(3)){
                    Message.message(this, "Du Gewinner! Shanghai Shanghai!");
                }

            }else{

                if( activePlayer == 0) activePlayer = 1;
                else{
                    roundCount++;
                    throwCount = 0;

                    if(roundCount > valArray.length){
                        Message.message(this, "Spiel zu Ende!");

                    }else{
                        roundCountView.setText(valArray[roundCount] + "");
                        for(int i = 0; i < 2; i++){
                            for(int k = 0; i < 3; i++){
                                playerDataList.get(i).scoreFieldViewList.get(k).setText(Integer.toString( (k+1) * valArray[roundCount]));
                            }
                        }
                    }

                }
            }
        }
    }

    public void onShanghaiMissButton(View view){

    }

    public boolean checkForShanghai(int val){
        boolean check = false;
        for(int i = 0; i < shanghaiArray.length; i++){
            if(shanghaiArray[i] == valArray[i] * (i+val)) check = true;
        }

        return check;
    }









    private class PlayerDataHolder{

        private Context c;
        int score = 0;
        List<TextView> scoreFieldViewList;
        TextView scoreView;
        int id;


        public PlayerDataHolder(Context con, int playerId){
            c = con;

            id = playerId;
            scoreView = (TextView) findViewById((QuickCombatShanghaiActivity.this.getResources().getIdentifier("shanghai_score_player_" + id, "id", QuickCombatShanghaiActivity.this.getPackageName())));
            scoreFieldViewList = new ArrayList<>();


            for(int i = 0; i < 3; i++) {
                scoreFieldViewList.add((TextView) findViewById(QuickCombatShanghaiActivity.this.getResources().getIdentifier("shanghai_score_field_" + (i+1) + "_pl_" + id, "id", QuickCombatShanghaiActivity.this.getPackageName()) ));
            }

        }


        public void addNewScoreValue(int val){
            score += val;
            scoreView.setText(Integer.toString(score));
        }
    }



    private void iniViews(){

        throwCountView = (TextView) findViewById(R.id.shanghai_throw_count);
        roundCountView = (TextView) findViewById(R.id.shanghai_round_count);
    }

    private void iniVals(Context c){
        int playerNum = 2;

        Bundle b = new Bundle();
        if( b != null){
            playerNum = b.getInt("PLAYER_NUM");
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
