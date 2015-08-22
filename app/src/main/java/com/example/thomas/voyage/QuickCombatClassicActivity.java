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

public class QuickCombatClassicActivity extends Activity implements ClassicWorkoutFragment.OnFragmentInteractionListener{

    private int multi = 1;
    ImageButton workoutImageView, versusImageView, historicalImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_classic);
        hideSystemUI();

        workoutImageView = (ImageButton) findViewById(R.id.classic_image_workout);
        versusImageView = (ImageButton) findViewById(R.id.classic_image_versus);
        historicalImageView = (ImageButton) findViewById(R.id.classic_image_historical);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ClassicWorkoutFragment fragment = new ClassicWorkoutFragment();
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
    };


    public void classicOnBackPressed(View view){
        super.onBackPressed();
    }

    public void onClassicScoreField(View view){

        int scoreFieldVal = 0;

        switch (view.getId()){
            case R.id.classic_scorefield_0:
                scoreFieldVal = 1; break;

            default:
                scoreFieldVal = -1;
        }
    }

    public void onClassicSetMultiplier(View view){

        switch (view.getId()){
            case R.id.classic_multi_1: multi = 1; break;
            case R.id.classic_multi_2: multi = 2; break;
            case R.id.classic_multi_3: multi = 3; break;
            default:
                Message.message(this,"ERROR @ onClassicSetMultiplier : wrong view id");
        }
    }

    public void onClassicMiss(View view){

    }

    public void onClassicUndo(View view){

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
