package com.example.thomas.voyage.ContainerClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.thomas.voyage.CombatActivities.CombatSplashActivity;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class IntentExtrasHelper {

    private ConstRes c = new ConstRes();

    public Intent toCombatSplash(Context c, long heroDbIndex, String biome, String level, int length){
        Intent i = new Intent(c, CombatSplashActivity.class);
        i.putExtra(this.c.HERO_DATABASE_INDEX, heroDbIndex);
        i.putExtra(this.c.CURRENT_BIOME, biome);
        i.putExtra(this.c.COMBAT_LEVEL_OF_MONSTERS, level);
        i.putExtra(this.c.COMBAT_LENGTH, length);

        return i;
    }
}
