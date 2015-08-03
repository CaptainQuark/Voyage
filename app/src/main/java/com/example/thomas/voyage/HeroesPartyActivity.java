package com.example.thomas.voyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class HeroesPartyActivity extends Activity {

    private final int HEROS_IN_LISTVIEW = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes_party);
        hideSystemUI();

        final ListView listview = (ListView) findViewById(R.id.activity_heroes_party_listView);
        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2"};

        // ints noch nutzlos
        int[] ints = new int[HEROS_IN_LISTVIEW];
        for (int i = 1; i <= HEROS_IN_LISTVIEW; i++) {
            ints[i - 1] = i;
        }

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                DBheroesAdapter heroesHelper = new DBheroesAdapter(getApplicationContext());
                String data = heroesHelper.getOneHeroRow(position + 1);

                Message.message(getApplicationContext(), data);
            }

        });
    }

    public void activityHeroesPartyBackToStart(View view) {
        Intent i = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(i);
        finish();
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

class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    DBheroesAdapter heroesHelper;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;

        heroesHelper = new DBheroesAdapter(this.context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView_rowlayout);

        if (heroesHelper.getHeroName(position + 1).equals("NOT_USED")) {
            imageView.setImageResource(R.mipmap.hero_dummy_gnu);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        return rowView;
    }
}
