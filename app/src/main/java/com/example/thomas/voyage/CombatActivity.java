package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CombatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        hideSystemUI();

        int[] prgmNameList = new int[30];
        for (int i = 0; i < 30; i++) {
            prgmNameList[i] = i + 1;
        }

        GridView gridView = (GridView) findViewById(R.id.activity_combat_gridView);
        gridView.setAdapter(new CustomAdapter(this, prgmNameList));
    }

    public void activityCombatBackToMain(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
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

    private static class CustomAdapter extends BaseAdapter {

        int[] result;
        int posCorrectionModFive = 0;
        Context context;
        private LayoutInflater inflater = null;

        public CustomAdapter(CombatActivity combatActivity, int[] prgmNameList) {
            result = prgmNameList;
            context = combatActivity;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return result.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();

            View rowView;

            rowView = inflater.inflate(R.layout.gridview_combat, null);
            holder.tv = (TextView) rowView.findViewById(R.id.gridView_combat_textView);

            holder.tv.setText(result[position] + "");

            if ((((result[position] - posCorrectionModFive) % (5 * (posCorrectionModFive + 1)) == 0))) {
                Log.v(" - MODULUS 5", "correctionInt: " + posCorrectionModFive + ", result[position]: " + result[position]);
                posCorrectionModFive++;
                holder.tv.setBackgroundColor(context.getResources().getColor(R.color.highlight_cherryred));
            } else if ((((result[position]) % 6) == 0)) {
                holder.tv.setBackgroundColor(context.getResources().getColor(R.color.highlight_cherryred));
            }

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Message.message(context, "You Clicked " + result[position]);
                }
            });

            return rowView;
        }

        public class Holder {
            TextView tv;
        }
    }
}


