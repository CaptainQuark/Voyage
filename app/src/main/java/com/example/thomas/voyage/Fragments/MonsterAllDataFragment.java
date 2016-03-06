package com.example.thomas.voyage.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ConstRes;

public class MonsterAllDataFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MonsterAllDataFragment() {
        // Required empty public constructor
    }

    public static MonsterAllDataFragment newInstance() {
        return new MonsterAllDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_monster_all_data, container, false);

        ImageView profileView = (ImageView) rootView.findViewById(R.id.frag_iv_monster_all_data_profile);
        TextView nameView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_name);
        TextView diffView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_difficulty);
        TextView checkoutView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_checkout);
        TextView hitpointsView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_hitpoints);
        TextView dmgMaxView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_dmg_max);
        TextView dmgMinView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_dmg_min);
        TextView blockView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_block);
        TextView evasionView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_evasion);
        TextView resistanceView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_resistance);
        TextView critMultiView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_crit_multi);
        TextView critChanceView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_crit_chance);
        TextView accuracyView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_accuracy);
        TextView bountyView = (TextView) rootView.findViewById(R.id.frag_iv_monster_all_data_bounty);

        ConstRes c = new ConstRes();
        Bundle b = getArguments();
        if(b != null){
            profileView.setImageResource(getActivity().getResources().getIdentifier(b.getString(c.MONSTER_IMG_RES), "mipmap", getActivity().getPackageName()));
            nameView.setText(b.getString(c.MONSTER_NAME));
            diffView.setText(b.getString(c.MONSTER_DIFFICULTY));
            checkoutView.setText(b.getString(c.MONSTER_CHECKOUT));
            hitpointsView.setText(b.getInt(c.MONSTER_HITPOINTS_NOW) + " / " + b.getInt(c.MONSTER_HITPOINTS_TOTAL));
            dmgMaxView.setText(String.valueOf(b.getInt(c.MONSTER_DAMAGE_MAX)));
            dmgMinView.setText(String.valueOf(b.getInt(c.MONSTER_DAMAGE_MIN)));
            blockView.setText(String.valueOf(b.getInt(c.MONSTER_ARMOR)));
            evasionView.setText(String.valueOf((1000 - (b.getInt(c.MONSTER_EVASION)))/10) + " %");
            resistanceView.setText(String.valueOf(b.getDouble(c.MONSTER_RESISTANCE)));
            critMultiView.setText(String.valueOf(b.getDouble(c.MONSTER_CRIT_MULTIPLIER)));
            critChanceView.setText(String.valueOf((1000 - (b.getInt(c.MONSTER_CRIT_CHANCE)))/10) + " %");
            accuracyView.setText(String.valueOf(b.getInt(c.MONSTER_ACCURACY) / 10) + " %");
            bountyView.setText(String.valueOf(b.getInt(c.MONSTER_BOUNTY)));
        }

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        return rootView;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.putMonsterAllDataFragToSleep();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void putMonsterAllDataFragToSleep();
    }
}
