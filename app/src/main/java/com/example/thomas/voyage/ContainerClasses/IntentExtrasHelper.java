package com.example.thomas.voyage.ContainerClasses;

import android.content.Context;
import android.content.Intent;

import com.example.thomas.voyage.CombatActivities.CombatMonsterHeroActivity;
import com.example.thomas.voyage.CombatActivities.CombatSplashActivity;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class IntentExtrasHelper {

    /*

    Wird nicht verwendet, um nur Ursprung weiterzugeben
    (ist bereits nur eine Zeile Code)

    Methoden sind statisch, da sie die Erzeugung eines
    Objekts nicht notwendig machen -> eine Zeile weniger Code

     */

    private ConstRes c = new ConstRes();

    public static Intent toCombatSplash(Context context, ConstRes c, long heroDbIndex, String biome, String level, int length){
        Intent i = new Intent(context, CombatSplashActivity.class);
        i.putExtra(c.HERO_DATABASE_INDEX, heroDbIndex);
        i.putExtra(c.CURRENT_BIOME, biome);
        i.putExtra(c.COMBAT_LEVEL_OF_MONSTERS, level);
        i.putExtra(c.COMBAT_LENGTH, length);

        return i;
    }

    public static Intent toCombatMonsterHero(Context context, ConstRes c, Monster monster, long heroIndex, String biome, String level, int length){

        Intent i = new Intent(context, CombatMonsterHeroActivity.class);
        i.putExtra(c.HERO_DATABASE_INDEX, heroIndex);
        i.putExtra(c.CURRENT_BIOME,biome);
        i.putExtra(c.COMBAT_LEVEL_OF_MONSTERS,level);
        i.putExtra(c.COMBAT_LENGTH, length);
        i.putExtra(c.MONSTER_NAME, monster.name);
        i.putExtra(c.MONSTER_DESCRIPTION, "");
        //i.putExtra(c.MONSTER_IMG_RES, "placeholder_dummy_0");
        i.putExtra(c.MONSTER_CHECKOUT, monster.checkout);
        i.putExtra(c.MONSTER_IMG_RES, monster.imgRes);
        i.putExtra(c.MONSTER_HITPOINTS_NOW, monster.hp);
        i.putExtra(c.MONSTER_HITPOINTS_TOTAL, monster.hpTotal);
        i.putExtra(c.MONSTER_DAMAGE_MIN, monster.dmgMin);
        i.putExtra(c.MONSTER_DAMAGE_MAX, monster.dmgMax);
        i.putExtra(c.MONSTER_BLOCK, monster.block);
        i.putExtra(c.MONSTER_EVASION, monster.evasion);
        i.putExtra(c.MONSTER_ACCURACY, monster.accuracy);
        i.putExtra(c.MONSTER_CRIT_CHANCE, monster.critChance);
        i.putExtra(c.MONSTER_CRIT_MULTIPLIER, monster.critMultiplier);
        i.putExtra(c.MONSTER_RESISTANCE, monster.resistance);
        i.putExtra(c.MONSTER_DIFFICULTY, monster.monsterDifficulty);
        i.putExtra(c.MONSTER_BOUNTY, monster.bounty);

        return i;
    }
}
