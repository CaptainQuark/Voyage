package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.HelperCSV;
import com.example.thomas.voyage.ContainerClasses.Hero;
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.Databases.DBmerchantHeroesAdapter;
import com.example.thomas.voyage.R;

import java.util.List;


public class HeroAllDataCardFragment extends Fragment implements View.OnClickListener{

    private onHeroAllDataCardListener mListener;
    private Hero hero;
    private int indexPlayer, indexMerch;

    public HeroAllDataCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle args = getArguments();
            indexPlayer = args.getInt("DB_INDEX_PLAYER", -1);
            indexMerch = args.getInt("DB_INDEX_MERCH", -1);

        }
        else{
            Msg.msg(getActivity(), "ERROR  getArguments in Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hero_all_data_card, container, false);

        ImageView profileView = (ImageView) rootView.findViewById(R.id.frag_imageview_hero_all_data_profile);
        TextView nameView = (TextView) rootView.findViewById(R.id.frag_textview_hero_name);
        TextView classesView = (TextView) rootView.findViewById(R.id.frag_textview_classes);
        TextView hpView = (TextView) rootView.findViewById(R.id.frag_textview_hp);
        TextView marketValueView = (TextView) rootView.findViewById(R.id.frag_textview_sell_value);
        TextView battlesView = (TextView) rootView.findViewById(R.id.frag_textview_bonus_number);
        TextView evasionView = (TextView) rootView.findViewById(R.id.frag_textview_evasion);
        TextView critChanceView = (TextView) rootView.findViewById(R.id.frag_textview_crit_chance);
        TextView critMultiplierView = (TextView) rootView.findViewById(R.id.frag_textview_crit_multiplier);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.frag_textview_hero_chronic);

        hero = new Hero(getActivity());

        if(indexMerch != -1) hero.copyHeroDataFromMerchantDatabase(indexMerch);
        else if(indexPlayer != -1) hero.copyHeroDataFromPlayerDatabase(indexPlayer);

        profileView.setImageResource(getActivity().getResources().getIdentifier(hero.getImageResource(), "mipmap", getActivity().getPackageName()));
        nameView.setText(hero.getHeroName());
        classesView.setText(hero.getClassPrimary() + " & " + hero.getClassSecondary());
        hpView.setText(hero.getHp() + " / " + hero.getHpTotal());
        marketValueView.setText(String.valueOf(hero.getCosts()));
        if(hero.getBonusNumber() == -1) {
            battlesView.setText("N / A");
        }else{
            battlesView.setText(String.valueOf(hero.getBonusNumber()));
        }
        evasionView.setText(String.valueOf((1000 - hero.getEvasion())/10) + " %");

        HelperCSV helperCSV = new HelperCSV(getContext());
        List<String[]> list = helperCSV.getDataList("heroresourcetable");
        String desc = "ERROR@HEROALLDATACARDFRAGMENT-descriptionView";
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals(hero.getClassPrimary())){
                desc = list.get(i)[15];
            }
        }
        for(int t = 0; t < list.size(); t++){
            if(list.get(t)[2].equals(hero.getClassSecondary())){
                desc = desc + '\n' + '\n' + list.get(t)[15];
            }
        }
        descriptionView.setText(desc);

        String critChance = "ERROR@HEROALLDATACARDFRAGMENT-critChanceView";
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals(hero.getClassPrimary())){
                critChance = String.valueOf((1000 - Integer.parseInt(list.get(i)[10]))/10) + " % / ";

            }
        }
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals(hero.getClassSecondary())){
                critChance = critChance + String.valueOf((1000 - Integer.parseInt(list.get(i)[10]))/10) + " %";
            }
        }
        critChanceView.setText(critChance);

        String critMultiplier = "ERROR@HEROALLDATACARDFRAGMENT-critMultiplierView";
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals((hero.getClassPrimary()))){
                critMultiplier = String.valueOf(list.get(i)[11] + " / ");

            }
        }
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals((hero.getClassSecondary()))){
                critMultiplier = critMultiplier + String.valueOf(list.get(i)[11]);
            }
        }
        critMultiplierView.setText(critMultiplier);
        profileView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onHeroAllDataCardListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onHeroAllDataInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_imageview_hero_all_data_profile:
                mListener.putFragmentToSleep();
                break;
            default:
                Msg.msgShort(getActivity(), "ERROR @ onClick : default at switch called");
        }
    }

    public interface onHeroAllDataCardListener {
        void putFragmentToSleep();
    }
}