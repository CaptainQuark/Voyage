package com.example.thomas.voyage.CombatActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.thomas.voyage.BasicActivities.HeroCampActivity;
import com.example.thomas.voyage.ContainerClasses.PassParametersHelper;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Fragments.ScreenSlidePageFragment;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;


public class WorldMapQuickCombatActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private ImageView heroProfile;
    private TextView goInCombat;
    private ConstRes c = new ConstRes();
    private int length;
    private String heroName = "", difficulty;
    private int index = -1;
    private boolean firstCheck = false, secondCheck = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_map_quick_combat);
        hideSystemUI();

        DBheroesAdapter h = new DBheroesAdapter(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            index = b.getInt(c.HERO_DATABASE_INDEX);
            Log.v("INDEX", "index : " + index);

            if(index != -1){
                heroName = h.getHeroName(index);
                heroProfile = (ImageView) findViewById(R.id.worldmap_imageView_hero_profile);
                heroProfile.setImageResource(getResources().getIdentifier(h.getHeroImgRes(index), "mipmap", getPackageName()));
                heroProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);

                TextView heroProfile = (TextView) findViewById(R.id.worldmap_textView_hero_name);
                heroProfile.setText(h.getHeroName(index));
            }
        }

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        goInCombat = (TextView) findViewById(R.id.worldmap_textView_go_in_combat);
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void WorldMapBackbuttonPressed(View view) {
        onBackPressed();
    }

    public void chooseHeroForCombat(View view){
        Intent i = new Intent(getApplicationContext(), HeroCampActivity.class);
        i.putExtra(c.ORIGIN, "WorldMapQuickCombatActivity");
        startActivity(i);
        finish();
    }

    // Erst sichtbar, wenn Level + Dauer gewählt sind
    public void goInCombat(View view){
        startActivity(PassParametersHelper.toCombatSplash(this, new ConstRes(), index, getCurrentBiome(), difficulty, length, 0, 0));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
         */
    }

    public void onRadioButtonClicked(View view) {

        /*



        Schwierigkeitsgrade:     1 - leichteste Stufe
                                 2 - mittel
                                 3 - schwierig


        Insgesamt zwei Gruppen:  1. - selectonArray[0} - Anspruch an Gegner
                                 2. - selectonArray[1} - Dauer


         */

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton_heavy_0:
                if (checked){
                    difficulty = "Easy";
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_heavy_1:
                if (checked){
                    difficulty = "Medium";
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_heavy_2:
                if (checked){
                    difficulty = "Hard";
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_length_0:
                if (checked){
                    length = 3;
                    secondCheck = true;
                }
                    break;
            case R.id.radioButton_length_1:
                if (checked){
                    length = 4;
                    secondCheck = true;
                }
                    break;
            case R.id.radioButton_lenght_2:
                if (checked){
                    length = 5;
                    secondCheck = true;
                }
                    break;
                default:
                    Msg.msg(this, "ERROR @ radioButtonClicked");
        }

        if (firstCheck && secondCheck && !heroName.equals(""))
            goInCombat.setVisibility(View.VISIBLE);
    }



    /*

    Funktionen

     */



    private String getCurrentBiome(){
        String biome;

        switch (mPager.getCurrentItem()){
            case 0:
                biome = "Forest";
                break;
            case 1:
                biome = "Icecave";
                break;
            case 3:
                biome = "Waterfall";
                break;
            default:
                biome = "Forest";
        }

        return biome;
    }


    /*

    Funktionen zur Auslagerungen

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

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putInt("INDEX", position);
            ScreenSlidePageFragment sc = new ScreenSlidePageFragment();
            sc.setArguments(bundle);

            return sc;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
