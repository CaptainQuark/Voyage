package com.example.thomas.voyage.CombatActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatActivity extends Activity {

    GridView cricketView;
    ConstRes c;
    String[] arrayDisplayPickerShanghai = {};
    int[] arrayOfSelectedValues, arrayValuesPickerShanghai;
    private List<Integer> selectionList = new ArrayList<>();
    ImageButton cricketImage, shanghaiImage, classicImage;
    NumberPicker npShanghai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat);
        hideSystemUI();
        iniViews();

        NumberPicker numberPickerCricket = (NumberPicker) findViewById(R.id.quick_numberpicker_cricket);
        npShanghai = (NumberPicker) findViewById(R.id.quick_shanghai_numberpicker);

        numberPickerCricket.setMaxValue(50);
        numberPickerCricket.setMinValue(1);
        numberPickerCricket.setValue(1);
        
        cricketView.setAdapter(new SimpleNumberAdapter(this));
        cricketView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        /*
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                v.setSelected(true);
                /*
                Toast.makeText(QuickCombatActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void quickImageTapped(View view){

        switch (view.getId()){

            case R.id.quick_imageview_cricket:

                cricketImage.setVisibility(View.INVISIBLE);
                shanghaiImage.setVisibility(View.VISIBLE);
                classicImage.setVisibility(View.VISIBLE);
                break;

            case R.id.quick_imageview_shanghai:

                cricketImage.setVisibility(View.VISIBLE);
                shanghaiImage.setVisibility(View.INVISIBLE);
                classicImage.setVisibility(View.VISIBLE);

                arrayValuesPickerShanghai = new int[20];
                arrayDisplayPickerShanghai = new String[arrayValuesPickerShanghai.length];

                for(int i = 0; i < arrayValuesPickerShanghai.length; i++){
                    arrayValuesPickerShanghai[i] = i+1;
                    arrayDisplayPickerShanghai[i] = Integer.toString(i+1);
                }

                npShanghai.setDisplayedValues(null);
                npShanghai.setMaxValue(arrayDisplayPickerShanghai.length - 1);
                npShanghai.setMinValue(0);
                npShanghai.setDisplayedValues(arrayDisplayPickerShanghai);
                npShanghai.setValue(6);
                break;

            case R.id.quick_imageview_classic:

                Intent i = new Intent(this, QuickCombatClassicActivity.class);
                startActivity(i);
                finish();
                /*
                cricketImage.setVisibility(View.VISIBLE);
                shanghaiImage.setVisibility(View.VISIBLE);
                classicImage.setVisibility(View.INVISIBLE);
                */
                break;

            default:
                Msg.msg(this, "ERROR @ quickImageTapped : wrong view.getId()");
        }
    }

    public void goToShanghai(View view){
        ConstRes c = new ConstRes();
        Intent i = new Intent(this, QuickCombatShanghaiActivity.class);
        i.putExtra(c.POINTS_TO_TRANSFER, arrayValuesPickerShanghai[npShanghai.getValue()]);
        startActivity(i);
    }

    public void goToCricket(View view){

        if(selectionList.isEmpty()){

            Msg.msg(this, "Keine Werte ausgewählt!");

        }else{

            arrayOfSelectedValues = new int[selectionList.size()];
            for(int i = 0; i < selectionList.size(); i++){
                arrayOfSelectedValues[i] = selectionList.get(i);
            }

            Intent i = new Intent(this, QuickCombatCricketActivity.class);
            i.putExtra("LIST_OF_SELECTED_VALUES", arrayOfSelectedValues);
            startActivity(i);
        }

    }

    public void quickCombatBackButton(View view){
        super.onBackPressed();
    }




    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            final TextView textView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                textView = new TextView(mContext);
                textView.setLayoutParams(new GridView.LayoutParams(115, 115));
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(getResources().getDrawable(R.drawable.buttonstates_combat_right_panel));
            } else {
                textView = (TextView) convertView;
            }

            textView.setText(Integer.toString(mThumbIds[position]));

            if (selectionList.contains(mThumbIds[position])) {
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(Color.BLACK);
            }

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    boolean finished = false;

                    if (!selectionList.contains(mThumbIds[position])) {

                        selectionList.add(mThumbIds[position]);
                        textView.setBackgroundColor(Color.BLACK);
                        textView.setTextColor(Color.WHITE);
                        //Msg.msg(getApplicationContext(), "size of list: " + selectionList.size());

                    } else {

                        textView.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(Color.BLACK);

                        for (int i = 0; i < selectionList.size(); i++) {
                            if (selectionList.get(i).equals(mThumbIds[position])) {
                                selectionList.remove(i);
                            }
                        }

                        //Msg.msg(getApplicationContext(), "size of list: " + selectionList.size());
                        cricketView.invalidateViews();
                    }
                }


            });

            return textView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,25
        };
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

    private void iniViews(){
        cricketImage = (ImageButton) findViewById(R.id.quick_imageview_cricket);
        shanghaiImage = (ImageButton) findViewById(R.id.quick_imageview_shanghai);
        classicImage = (ImageButton) findViewById(R.id.quick_imageview_classic);

        cricketView = (GridView) findViewById(R.id.quick_gridView_selected_numbers);
    }
}
