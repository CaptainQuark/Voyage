package com.example.thomas.voyage.CombatActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class CombatMonsterHeroActivity extends Activity {

    private final int numValues = 20;
    private List<ValuesContainer> valListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_monster_hero);
        hideSystemUI();

        valListContainer = new ArrayList<>();

        for(int i = 0; i < numValues; i++){
            valListContainer.add(new ValuesContainer(i));
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        hideSystemUI();
    }


    /*

    Funktionen

    */


    public void onClick(View v){

        for(int i = 0; i < numValues; i++){
            if(v.getId() == valListContainer.get(i).res){
                Msg.msg(getApplicationContext(), "Cell " + i + " tapped! Hurray!");
                valListContainer.get(i).setNewBackground();
            }
        }
    }


    /*

    Klassen

    */


    private class ValuesContainer{
        private TextView valView;
        private int res;

        public ValuesContainer(int index){
            res = getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName());
            valView = (TextView) findViewById(getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName()));
            valView.setText(String.valueOf(index + 1));
        }

        public void setNewBackground(){
            valView.setBackgroundColor(Color.BLUE);
        }
    }


    /*

    Auslagerung von Initialisierungen

     */


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
