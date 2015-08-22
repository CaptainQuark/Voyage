package com.example.thomas.voyage;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.Fragments.ClassicWorkoutFragment;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class QuickCombatClassicActivity extends Activity implements ClassicWorkoutFragment.OnFragmentInteractionListener{

    private int multi = 1;
    private ClassicWorkoutFragment fragment;
    private ImageButton workoutImageView, versusImageView, historicalImageView;
    private List<TextView> multiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_classic);
        hideSystemUI();

        workoutImageView = (ImageButton) findViewById(R.id.classic_image_workout);
        versusImageView = (ImageButton) findViewById(R.id.classic_image_versus);
        historicalImageView = (ImageButton) findViewById(R.id.classic_image_historical);

        TextView mulitOneView = (TextView) findViewById(R.id.classic_multi_1);
        TextView multiTwoView = (TextView) findViewById(R.id.classic_multi_2);
        TextView mulitThreeViw = (TextView) findViewById(R.id.classic_multi_3);
        multiList.add(mulitOneView);
        multiList.add(multiTwoView);
        multiList.add(mulitThreeViw);

        multiList.get(multi - 1).setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
        multiList.get(multi - 1).setTextColor(Color.BLACK);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ClassicWorkoutFragment();

        Bundle b = new Bundle();
        b.putInt("NUM_ROUND_TOTAL", 1);
        b.putInt("NUM_GOAL_POINTS", 501);
        fragment.setArguments(b);
        fragmentTransaction.add(R.id.classic_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }


    public void onFragmentInteraction(Uri uri){
        Message.message(this, "YIHA");
    }


    public void classicOnBackPressed(View view){
        super.onBackPressed();
    }

    public void onClassicScoreField(View view){

        int scoreFieldVal = 0;

        switch (view.getId()){
            case R.id.classic_scorefield_0:
                scoreFieldVal = 1; break;
            case R.id.classic_scorefield_1:
                scoreFieldVal = 2; break;
            case R.id.classic_scorefield_2:
                scoreFieldVal = 3; break;
            case R.id.classic_scorefield_3:
                scoreFieldVal = 4; break;
            case R.id.classic_scorefield_4:
                scoreFieldVal = 5; break;
            case R.id.classic_scorefield_5:
                scoreFieldVal = 6; break;
            case R.id.classic_scorefield_6:
                scoreFieldVal = 7; break;
            case R.id.classic_scorefield_7:
                scoreFieldVal = 8; break;
            case R.id.classic_scorefield_8:
                scoreFieldVal = 9; break;
            case R.id.classic_scorefield_9:
                scoreFieldVal = 10; break;
            case R.id.classic_scorefield_10:
                scoreFieldVal = 11; break;
            case R.id.classic_scorefield_11:
                scoreFieldVal = 12; break;
            case R.id.classic_scorefield_12:
                scoreFieldVal = 13; break;
            case R.id.classic_scorefield_13:
                scoreFieldVal = 14; break;
            case R.id.classic_scorefield_14:
                scoreFieldVal = 15; break;
            case R.id.classic_scorefield_15:
                scoreFieldVal = 16; break;
            case R.id.classic_scorefield_16:
                scoreFieldVal = 17; break;
            case R.id.classic_scorefield_17:
                scoreFieldVal = 18; break;
            case R.id.classic_scorefield_18:
                scoreFieldVal = 19; break;
            case R.id.classic_scorefield_19:
                scoreFieldVal = 20; break;
            case R.id.classic_scorefield_25:
                scoreFieldVal = 25; break;
            case R.id.classic_scorefield_50:
                scoreFieldVal = 50; break;
            default:
                Message.message(this, "DEFAULT @ onClassicScoreField");

        }

        if(scoreFieldVal != 25 && scoreFieldVal != 50){
            scoreFieldVal *= multi;
        }

        fragment.setOneThrow(scoreFieldVal);
    }

    public void onClassicSetMultiplier(View view){

        switch (view.getId()){
            case R.id.classic_multi_1: multi = 1; break;
            case R.id.classic_multi_2: multi = 2; break;
            case R.id.classic_multi_3: multi = 3; break;
            default:
                Message.message(this,"ERROR @ onClassicSetMultiplier : wrong view id");
        }

        for(int i = 0; i < multiList.size(); i++){
            if(i == (multi-1)){
                multiList.get(i).setBackground(getDrawable(R.drawable.ripple_from_darkgrey_to_black));
                multiList.get(i).setTextColor(Color.BLACK);
            }else{
                multiList.get(i).setBackgroundColor(Color.BLACK);
                multiList.get(i).setTextColor(Color.WHITE);
            }
        }

    }

    public void onClassicMiss(View view){
        fragment.setOneThrow(0);
    }

    public void onClassicUndo(View view){

        //TODO Implementation
    }

    public void onClassicSelectionImage(View view){

        switch (view.getId()){

            case R.id.classic_image_workout:
                workoutImageView.setVisibility(View.INVISIBLE);
                versusImageView.setVisibility(View.VISIBLE);
                historicalImageView.setVisibility(View.VISIBLE);
                break;

            case R.id.classic_image_versus:
                workoutImageView.setVisibility(View.VISIBLE);
                versusImageView.setVisibility(View.INVISIBLE);
                historicalImageView.setVisibility(View.VISIBLE);
                break;

            case R.id.classic_image_historical:
                workoutImageView.setVisibility(View.VISIBLE);
                versusImageView.setVisibility(View.VISIBLE);
                historicalImageView.setVisibility(View.INVISIBLE);
                break;

            default:
                Message.message(this, "ERROR @ onClassicSelectionImage : wrong view id");
                break;
        }
    }
















    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
