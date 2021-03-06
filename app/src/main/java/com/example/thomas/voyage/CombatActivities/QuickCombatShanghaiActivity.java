package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatShanghaiActivity extends Activity {

    private int activePlayer = 0, roundCount = 1, throwCount = 1;
    private int[] valArray;
    private boolean[] shanghaiArray = {false,false,false};
    private List<PlayerDataHolder> playerDataList;
    private List<ThrowDataHolder> throwDataList;
    private List<TextView> scoreFieldViewList = new ArrayList<>();
    private List<Integer> undoList = new ArrayList<>();
    private TextView throwCountView, roundCountView, undoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_shanghai);
        hideSystemUI();
        iniViews();
        iniVals(this);

        playerDataList = new ArrayList<>();
        throwDataList = new ArrayList<>();

        for(int i = 0; i < 2; i++) playerDataList.add( new PlayerDataHolder(this, (i+1)) );

        for(int i = 0; i < 3; i++) {
            scoreFieldViewList.add((TextView) findViewById(QuickCombatShanghaiActivity.this
                    .getResources().getIdentifier("shanghai_score_field_" + (i + 1), "id", QuickCombatShanghaiActivity.this.getPackageName()) ));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void onShanghaiScoreField(View view){
        int scoreMulti = -1,  numThrowsPerPlayerPerRound = 3;

        switch (view.getId()){

            case R.id.shanghai_score_field_3:
                scoreMulti = 3;
                break;
            case R.id.shanghai_score_field_2:
                scoreMulti = 2;
                break;
            case R.id.shanghai_score_field_1:
                scoreMulti = 1;
                break;
            case R.id.shanghai_miss_button:
                Msg.msg(this, "missButton");
                scoreMulti = 0;
                break;
            default:
                Msg.msg(this, "DEFAULT @ 'onShanghaiScoreField'");
                break;
        }

        if(roundCount <= valArray.length){
            playerDataList.get(activePlayer).addNewScoreValue(valArray[roundCount - 1] * scoreMulti);
            undoList.add(valArray[roundCount - 1] * scoreMulti);
            throwDataList.add( new ThrowDataHolder( valArray[roundCount-1], scoreMulti));
        }

        if(undoList.size() == 1) undoButton.setTextColor(Color.BLACK);

        if(scoreMulti > 0){
            shanghaiArray[scoreMulti-1] = true;

            if( activePlayer == 0) scoreFieldViewList.get(scoreMulti - 1).setBackground(getDrawable(R.drawable.ripple_round_player_one_field_finished));
            else scoreFieldViewList.get(scoreMulti - 1).setBackground(getDrawable(R.drawable.ripple_round_player_two_field_finished));

        }

        if(throwCount < numThrowsPerPlayerPerRound){
            throwCount++;
            throwCountView.setText(throwCount + ".");

        }else {
            if (shanghaiArray[0] && shanghaiArray[1] && shanghaiArray[2]) {
                Msg.msg(this, "Du Gewinner! Shanghai Shanghai!");

            } else {

                if (roundCount > valArray.length) {
                    Msg.msg(this, "Spiel zu Ende!");

                }else{

                    if(activePlayer == 0) {
                        activePlayer = 1;
                        for (int i = 0; i < scoreFieldViewList.size(); i++)
                            scoreFieldViewList.get(i).setBackground(getDrawable(R.drawable.ripple_round_player_two));
                    }else{
                        roundCount++;
                        if(roundCount > valArray.length) Msg.msg(this, "Spiel zu Ende!");

                        else {
                            for (int k = 0; k < 3; k++) scoreFieldViewList.get(k).setText(Integer.toString((k + 1) * valArray[roundCount-1]));
                            roundCountView.setText(valArray[roundCount - 1] + "");
                            activePlayer = 0;
                            for (int i = 0; i < scoreFieldViewList.size(); i++)
                                scoreFieldViewList.get(i).setBackground(getDrawable(R.drawable.ripple_round_player_one));
                        }
                    }

                    throwCount = 1;
                    throwCountView.setText(throwCount + ".");
                    for(int i = 0; i < shanghaiArray.length; i++) shanghaiArray[i] = false;

                }
            }
        }
    }

    public void onShanghaiBackButton(View view){
        super.onBackPressed();
        finish();
    }

    public void onShanghaiUndo(View view){
        if(!undoList.isEmpty()){

            throwCount--;

            if(throwCount == 0){
                throwCount = 3;

                if(activePlayer == 0){
                    activePlayer = 1;
                    roundCount--;
                    roundCountView.setText( valArray[roundCount-1] + "");
                    for (int k = 0; k < 3; k++) scoreFieldViewList.get(k).setText(Integer.toString((k + 1) * valArray[roundCount-1]));
                }
                else activePlayer = 0;
            }

            throwCountView.setText(throwCount + ".");
            playerDataList.get(activePlayer).reverseScoreValue(undoList.get(undoList.size() - 1));

            if(activePlayer == 0) scoreFieldViewList.get( throwDataList.get( throwDataList.size()-1).getMulti() -1 ).setBackground(getDrawable(R.drawable.ripple_round_player_one));
            else if(activePlayer == 1) scoreFieldViewList.get( throwDataList.get( throwDataList.size()-1).getMulti() -1 ).setBackground(getDrawable(R.drawable.ripple_round_player_two));

            throwDataList.remove( throwDataList.size() - 1 );
            undoList.remove( undoList.size() - 1 );

            if(undoList.isEmpty()) undoButton.setTextColor(Color.GRAY);

        }else{
            Msg.msg(this, "WA'SUP!!!\n...no more actions to undo...");

        }
    }

    private class ThrowDataHolder{
        int value;
        int multi;

        public ThrowDataHolder(int val, int multi){
            value = val;
            this.multi = multi;
        }

        public int getValue(){ return value; }

        public int getMulti(){ return multi; }
    }

     private class PlayerDataHolder{

        private Context c;
        int score = 0;
        TextView scoreView;
        int id;


        public PlayerDataHolder(Context con, int playerId){
            c = con;

            id = playerId;
            scoreView = (TextView) findViewById((QuickCombatShanghaiActivity.this.getResources().getIdentifier("shanghai_score_player_" + id, "id", QuickCombatShanghaiActivity.this.getPackageName())));
        }

        public void addNewScoreValue(int val){
            score += val;
            scoreView.setText(Integer.toString(score));
        }

         public void reverseScoreValue(int val){
             score -= val;
             scoreView.setText(Integer.toString(score));
         }
    }











    private void iniViews(){

        throwCountView = (TextView) findViewById(R.id.shanghai_throw_count);
        roundCountView = (TextView) findViewById(R.id.shanghai_round_count);
        undoButton = (TextView) findViewById(R.id.shanghai_undo_button);
    }

    private void iniVals(Context c){
        ConstRes constRes = new ConstRes();

        Bundle b = getIntent().getExtras();
        if( b != null){
            int length = b.getInt(constRes.POINTS_TO_TRANSFER);
            valArray = new int[length];
            for(int i = 0; i < length; i++) valArray[i] = i+1;

        }else{
            Msg.msg(this, "no bundle data recieved");
            valArray = new int[1];
            valArray[0] = 1;
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
