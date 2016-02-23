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
import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;

import org.w3c.dom.Text;

import java.util.List;


public class HeroAllDataCardFragment extends Fragment implements View.OnClickListener{

    private onHeroAllDataCardListener mListener;
    private int dbIndex;

    public HeroAllDataCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle args = getArguments();
            dbIndex = args.getInt("DB_INDEX");
        }
        else{
            Msg.msg(getActivity(), "ERROR  getArguments in Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hero_all_data_card, container, false);

        DBheroesAdapter h = new DBheroesAdapter(getActivity());

        ImageView profileView = (ImageView) rootView.findViewById(R.id.frag_imageview_hero_all_data_profile);
        TextView nameView = (TextView) rootView.findViewById(R.id.frag_textview_hero_name);
        TextView classesView = (TextView) rootView.findViewById(R.id.frag_textview_classes);
        TextView hpView = (TextView) rootView.findViewById(R.id.frag_textview_hp);
        TextView marketValueView = (TextView) rootView.findViewById(R.id.frag_textview_sell_value);
        TextView battlesView = (TextView) rootView.findViewById(R.id.frag_textview_bonus_number);
        TextView evasionView = (TextView) rootView.findViewById(R.id.frag_textview_evasion);
        TextView avgAttacksView = (TextView) rootView.findViewById(R.id.frag_textview_avg_attacks_per_battle);
        TextView levelView = (TextView) rootView.findViewById(R.id.frag_textview_level);
        TextView descriptionView = (TextView) rootView.findViewById(R.id.frag_textview_hero_chronic);

        profileView.setImageResource(getActivity().getResources().getIdentifier(h.getHeroImgRes(dbIndex), "mipmap", getActivity().getPackageName()));
        nameView.setText(h.getHeroName(dbIndex));
        classesView.setText(h.getHeroPrimaryClass(dbIndex) + " & " + h.getHeroSecondaryClass(dbIndex));
        hpView.setText(h.getHeroHitpoints(dbIndex) + " / " + h.getHeroHitpointsTotal(dbIndex));
        marketValueView.setText(String.valueOf(h.getHeroCosts(dbIndex)));
        if(h.getBonusNumber(dbIndex) == -1) {
            battlesView.setText("N / A");
        }else{
            battlesView.setText(String.valueOf(h.getBonusNumber(dbIndex)));
        }
        evasionView.setText(String.valueOf(h.getEvasion(dbIndex)));
        avgAttacksView.setText("?");
        levelView.setText("?");

        HelperCSV helperCSV = new HelperCSV(getContext());
        List<String[]> list = helperCSV.getDataList("heroresourcetable");
        String desc = "ERROR@HEROALLDATACARDFRAGMENT-descriptionView";
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)[2].equals(h.getHeroPrimaryClass(dbIndex))){
                desc = list.get(i)[15];
            }
        }
        for(int t = 0; t < list.size(); t++){
            if(list.get(t)[2].equals(h.getHeroSecondaryClass(dbIndex))){
                desc = desc + '\n' + '\n' + list.get(t)[15];
            }
        }
        descriptionView.setText(desc);

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