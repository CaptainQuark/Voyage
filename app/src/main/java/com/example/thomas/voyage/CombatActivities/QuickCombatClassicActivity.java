package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Fragments.ClassicWorkoutFragment;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatClassicActivity extends Activity implements ClassicWorkoutFragment.OnFragmentInteractionListener{

    private int multi = 1;
    private boolean saveToStats = true;
    private ClassicWorkoutFragment fragment;
    private ImageButton workoutImageView, versusImageView, historicalImageView;
    private LinearLayout gameView, optionsView;
    private RelativeLayout selectImageView, selectPropertiesView;
    private FrameLayout selectView;
    private NumberPicker roundPicker, pointPicker;
    private TextView noStatsRecordingView, activateGhostRecordView;
    private List<TextView> multiList = new ArrayList<>();
    private String[] stringArray = {"101","301","501","1001"};
    private int[] stringArrayPartner = {101,301,501,1001};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_classic);
        hideSystemUI();

        optionsView = (LinearLayout) findViewById(R.id.classic_linearlayout_additional_options);

        workoutImageView = (ImageButton) findViewById(R.id.classic_image_workout);
        versusImageView = (ImageButton) findViewById(R.id.classic_image_versus);
        historicalImageView = (ImageButton) findViewById(R.id.classic_image_historical);

        TextView mulitOneView = (TextView) findViewById(R.id.classic_multi_1);
        TextView multiTwoView = (TextView) findViewById(R.id.classic_multi_2);
        TextView mulitThreeViw = (TextView) findViewById(R.id.classic_multi_3);

        selectImageView = (RelativeLayout) findViewById(R.id.classic_relativelayout_image_selection);
        selectPropertiesView = (RelativeLayout) findViewById(R.id.classic_relativelayout_properties_selection);
        gameView = (LinearLayout) findViewById(R.id.classic_linearlayout_session_layout);
        selectView = (FrameLayout) findViewById(R.id.classic_framelayout_choose_session_type);
        noStatsRecordingView = (TextView) findViewById(R.id.classic_textview_prohib_from_recording);
        activateGhostRecordView = (TextView) findViewById(R.id.classic_textview_record_as_ghost);

        multiList.add(mulitOneView);
        multiList.add(multiTwoView);
        multiList.add(mulitThreeViw);

        multiList.get(multi - 1).setBackground(getDrawable(R.drawable.ripple_grey_to_black));
        multiList.get(multi - 1).setTextColor(Color.BLACK);

        roundPicker = (NumberPicker) findViewById(R.id.quick_roundpicker_classic_workout);
        roundPicker.setMaxValue(50);
        roundPicker.setMinValue(1);
        roundPicker.setValue(1);

        pointPicker = (NumberPicker) findViewById(R.id.quick_pointpicker_classic_workout);
        pointPicker.setDisplayedValues(null);
        pointPicker.setMaxValue(stringArray.length - 1);
        pointPicker.setMinValue(0);
        pointPicker.setDisplayedValues(stringArray);
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    @Override
    public void dismissRecordButtons(boolean setVisible) {
        if(setVisible){
            optionsView.setVisibility(View.VISIBLE);

        }else{
            optionsView.setVisibility(View.INVISIBLE);
        }
    }

    public void putFragmentToSleep(){
        if(fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
        Message.message(this, "Fragment removed");
        gameView.setVisibility(View.GONE);
        selectView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean getSaveStatsChoice() {
        boolean temp = saveToStats;
        saveToStats = true;

        return temp;
    }

    public void classicWorkoutDisableStatsRecording(View view){
        saveToStats = false;
        Message.message(this, "No recording for this round");
    }

    public void classicWorkoutRecordGhost(View view){
        Message.message(this, "WA'SUP!!!\n...no ghost yet implemented...");
    }

    public void goToClassicWorkout(View view){

        /*
        switch (pointPicker.getValue()){
            case 0:
                points = 301; break;
            case 1:
                points = 501; break;
            case 2:
                points = 1001; break;
            default:
                points = -1; break;
        }
        */

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ClassicWorkoutFragment();
        Bundle b = new Bundle();
        b.putInt("NUM_ROUND_TOTAL", roundPicker.getValue());
        b.putInt("NUM_GOAL_POINTS", stringArrayPartner[pointPicker.getValue()]);
        fragment.setArguments(b);
        fragmentTransaction.add(R.id.classic_fragment_container, fragment);
        fragmentTransaction.commit();

        gameView.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.GONE);
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


        if(scoreFieldVal == 25 || scoreFieldVal == 50){
            multi = 1;
        }

        fragment.setOneThrow(scoreFieldVal, multi);
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
                multiList.get(i).setBackground(getDrawable(R.drawable.ripple_grey_to_black));
                multiList.get(i).setTextColor(Color.BLACK);
            }else{
                multiList.get(i).setBackgroundColor(Color.BLACK);
                multiList.get(i).setTextColor(Color.WHITE);
            }
        }

    }

    public void onClassicMiss(View view){
        fragment.setOneThrow(0, 0);
    }

    public void onClassicUndo(View view){
        fragment.undoLastThrow();
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
