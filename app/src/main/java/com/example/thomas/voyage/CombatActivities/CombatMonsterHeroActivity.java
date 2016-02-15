package com.example.thomas.voyage.CombatActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Monster;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class CombatMonsterHeroActivity extends Activity {

    private final int numValues = 20;
    private int lastSelectedValIndex = -1, monsterHpNow, monsterHpTotal;
    private List<ValuesContainer> valListContainer;
    private TextView lastSelectedValView, monsterHpView, heroHpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat_monster_hero);
        hideSystemUI();
        iniViews();

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

    OnClick - Methoden

     */



    public void onClick(View v){
        switch (v.getId()){
            case R.id.cell_com_miss:
                Msg.msg(this, "You missed dat one!");
                break;
        }
    }

    public void onMultiClick(View v){
        switch (v.getId()){
            case R.id.cell_com_multi_single_in:
                break;
            case R.id.cell_com_multi_single_out:
                break;
            case R.id.cell_com_multi_x_2:
                break;
            case R.id.cell_com_multi_x_3:
                break;
            case R.id.cell_com_multi_bull:
                break;
            case R.id.cell_com_multi_eye:
                break;
            default:
                Msg.msg(this, "ERROR @ onMultiClick : default called");
        }

    }



    /*

    Funktionen

    */



    // Code



    /*

    Klassen

    */



    private class ValuesContainer{
        private TextView valView;
        private int res, index;
        private boolean isActive = false;

        public ValuesContainer(final int index){
            this.index = index;
            res = getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName());
            valView = (TextView) findViewById(getResources().getIdentifier("cell_com_val_" + index, "id", getPackageName()));
            valView.setText(String.valueOf(index + 1));

            valView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!(lastSelectedValView == null) && lastSelectedValIndex != index)
                        lastSelectedValView.setBackground(getResources().getDrawable(R.drawable.ripple_grey_to_black, null));

                    lastSelectedValIndex = index;
                    lastSelectedValView = (TextView) view;

                    view.setBackgroundResource((isActive = !isActive) ? R.drawable.ripple_from_darkgrey_to_black : R.drawable.ripple_grey_to_black);
                }
            });
        }
    }



    /*

    Auslagerung von Initialisierungen

     */



    private void iniViews(){
        ImageView monsterProfileView = (ImageView) findViewById(R.id.imageview_com_monster_profile);
        ImageView heroProfileView = (ImageView) findViewById(R.id.imageview_com_hero_profile);
        TextView monsterNameView = (TextView) findViewById(R.id.textview_com_monster_name);
        TextView heroNameView = (TextView) findViewById(R.id.textview_com_hero_name);
        monsterHpView = (TextView) findViewById(R.id.textview_com_monster_hp_now);
        heroHpView = (TextView) findViewById(R.id.textview_com_hero_hp_now);

        Monster monster = new Monster();
        monsterProfileView.setImageResource(getResources().getIdentifier(monster.getImgRes(), "mipmap", getPackageName()));
        monsterNameView.setText(monster.name);

        ConstRes c = new ConstRes();
        Bundle b = getIntent().getExtras();
        if(b != null){
            heroProfileView.setImageResource(getResources().getIdentifier(b.getString(c.IMAGE_RESOURCE), "mipmap", getPackageName()));
            heroNameView.setText(b.getString(c.HEROES_NAME, "Name"));
            heroHpView.setText(String.valueOf(b.getInt(c.HEROES_HITPOINTS, -1)));
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
