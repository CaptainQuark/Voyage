package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuickCombatCricket extends Activity {

    int count = 21;
    int tempCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_combat_cricket);
        hideSystemUI();


        GridView cricketView = (GridView) findViewById(R.id.cricket_gridview);
        cricketView.setAdapter(new SimpleNumberAdapter(this));
    }











    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;
            final TextView tv;


            if (convertView == null) {

                gridView = new View(mContext);
                gridView = inflater.inflate( R.layout.quick_cricket_card , parent, false);

                tv = (TextView) gridView.findViewById(R.id.quick_cricket_card_textview);

            } else {
                gridView = (View) convertView;
            }


            gridView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //tv.setBackgroundColor(Color.BLUE);
                    //Message.message(getApplicationContext(), "size of list: " + selectionList.size());
                }
            });

            return gridView;
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
