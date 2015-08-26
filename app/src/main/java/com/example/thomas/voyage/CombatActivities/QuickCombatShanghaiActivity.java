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

    private int activePlayer = 0;
    private List<PlayerDataHolder> playerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_shanghai);
        hideSystemUI();
        iniViews();
        iniVals(this);

        playerDataList = new ArrayList<>();
        for(int i = 0; i < 2; i++) playerDataList.add( new PlayerDataHolder(this, (i+1)) );

        //TextView tv = (TextView) findViewById(getResources().getIdentifier("shanghai_score_field_" + 2 + "_pl_" + 2, "id", getPackageName()) );
        //tv.setText("JA");
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void onShanghaiScoreField(View view){
        switch (view.getId()){
            case R.id.shanghai_score_field_3_pl_1:
                Message.message(this, "scoreList Player 1: " + playerDataList.get(0).scoreList.get(0) + "");
                playerDataList.get(0).scoreFieldViewList.get(2).setText("JA");
        }
    }










    private class PlayerDataHolder{

        Context c;
        List<Integer> scoreList;
        List<TextView> scoreFieldViewList;
        int id;


        public PlayerDataHolder(Context con, int playerId){
            c = con;

            id = playerId;
            scoreList = new ArrayList<>();
            scoreFieldViewList = new ArrayList<>();

            for(int i = 0; i < 3; i++) {
                scoreList.add(0);
                scoreFieldViewList.add((TextView) findViewById(QuickCombatShanghaiActivity.this.getResources().getIdentifier("shanghai_score_field_" + (i+1) + "_pl_" + id, "id", QuickCombatShanghaiActivity.this.getPackageName()) ));
            }
        }
    }



    private void iniViews(){

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
