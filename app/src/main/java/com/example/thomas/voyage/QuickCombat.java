package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuickCombat extends Activity {

    GridView cricketView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat);
        hideSystemUI();

        cricketView = (GridView) findViewById(R.id.quick_gridView_selected_numbers);
        NumberPicker numberPickerCricket = (NumberPicker) findViewById(R.id.quick_numberpicker_cricket);
        numberPickerCricket.setMaxValue(50);
        numberPickerCricket.setMinValue(1);
        numberPickerCricket.setValue(1);
        numberPickerCricket.setWrapSelectorWheel(false);
        cricketView.setAdapter(new SimpleNumberAdapter(this));
        cricketView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        /*
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                v.setSelected(true);
                /*
                Toast.makeText(QuickCombat.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first
        hideSystemUI();
    }

    public void goToCricket(View view){
        Intent i = new Intent(this, QuickCombatCricket.class);
        startActivity(i);
        finish();
    }





    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

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
                        selectionList.add(position + 1);
                        textView.setBackgroundColor(Color.BLACK);
                        textView.setTextColor(Color.WHITE);
                        Message.message(getApplicationContext(), "size of list: " + selectionList.size());
                    } else {
                        textView.setBackgroundColor(Color.TRANSPARENT);
                        textView.setTextColor(Color.BLACK);


                        for (int i = 0; i < selectionList.size(); i++) {
                            if (selectionList.get(i) == mThumbIds[position]) {
                                selectionList.remove(i);
                            }
                        }

                        Message.message(getApplicationContext(), "size of list: " + selectionList.size());
                        cricketView.invalidateViews();
                    }
                }


            });

            return textView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21
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
}
