package com.example.thomas.voyage.CombatActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.thomas.voyage.BasicActivities.HeroCampActivity;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Fragments.ScreenSlidePageFragment;
import com.example.thomas.voyage.BasicActivities.HeroesPartyActivity;
import com.example.thomas.voyage.R;


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
    private String image = "R.id.hero_dummy_0", heroName = "", primClass = "", secClass = "";
    private int hitpoints = -1, costs = -1, evasion = -1;
    private int[] difficulty;
    private boolean firstCheck = false, secondCheck = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_map_quick_combat);
        hideSystemUI();

        difficulty = new int[2];

        Bundle b = getIntent().getExtras();
        if(b != null){
            image = b.getString("IMAGE_RESOURCE", "");
            heroName = b.getString("HEROES_NAME", "");
            primClass = b.getString("HEROES_PRIMARY_CLASS", "");
            secClass = b.getString("HEROES_SECONDARY_CLASS", "");
            hitpoints = b.getInt("HEROES_HITPOINTS", -1);
            costs = b.getInt("HEROES_COSTS", -1);
            evasion = b.getInt("EVASION", -1);

            heroProfile = (ImageView)findViewById(R.id.worldmap_imageView_hero_profile);
            heroProfile.setImageResource(getResources().getIdentifier(image, "mipmap", getPackageName()));
            heroProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        TextView heroProfile = (TextView) findViewById(R.id.worldmap_textView_hero_name);
        heroProfile.setText(heroName);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        goInCombat = (TextView)findViewById(R.id.worldmap_textView_go_in_combat);
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
        i.putExtra("ORIGIN", "WorldMapQuickCombatActivity");
        startActivity(i);
        finish();
    }

    public void goInCombat(View view){
        Intent i = new Intent(getApplicationContext(), CombatActivity.class);
        i.putExtra("ORIGIN", "WorldMapQuickCombatActivity");
        i.putExtra("HEROES_NAME", heroName);
        i.putExtra("HEROES_PRIMARY_CLASS", primClass);
        i.putExtra("HEROES_SECONDARY_CLASS",secClass);
        i.putExtra("HEROES_HITPOINTS",hitpoints);
        i.putExtra("HEROES_COSTS", costs);
        i.putExtra("IMAGE_RESOURCE",image);
        i.putExtra("EVASION", evasion);
        startActivity(i);
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


        Insgesamt zwei Gruppen:  1. - difficulty[0} - Anspruch an Gegner
                                 2. - difficulty[1} - Dauer


         */

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton_heavy_0:
                if (checked){
                    difficulty[0] = 1;
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_heavy_1:
                if (checked){
                    difficulty[0] = 2;
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_heavy_2:
                if (checked){
                    difficulty[0] = 3;
                    firstCheck = true;
                }
                    break;
            case R.id.radioButton_length_0:
                if (checked){
                    difficulty[1] = 1;
                    secondCheck = true;
                }
                    break;
            case R.id.radioButton_length_1:
                if (checked){
                    difficulty[1] = 2;
                    secondCheck = true;
                }
                    break;
            case R.id.radioButton_lenght_2:
                if (checked){
                    difficulty[1] = 3;
                    secondCheck = true;
                }
                    break;
                default:
                    Msg.msg(this, "ERROR @ radioButtonClicked");
        }

        if (firstCheck && secondCheck && !heroName.equals(""))
            goInCombat.setVisibility(View.VISIBLE);
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
