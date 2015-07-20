package com.example.thomas.voyage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        View view = findViewById(R.id.activity_start_landscape);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            // zuerst der Activity eine id geben -> danach direkt ansprechbar
    }

}
