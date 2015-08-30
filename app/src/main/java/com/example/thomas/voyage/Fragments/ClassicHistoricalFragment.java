package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBghostScoreDataAdapter;
import com.example.thomas.voyage.Databases.DBheroesAdapter;
import com.example.thomas.voyage.R;
import com.example.thomas.voyage.ResClasses.ImgRes;

import java.util.List;


public class ClassicHistoricalFragment extends Fragment {

    DBghostScoreDataAdapter scoresAdapter;

    public ClassicHistoricalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classic_historical, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_historical);

        scoresAdapter = new DBghostScoreDataAdapter(getActivity());
        int sizeOfGame = (int) scoresAdapter.getRowsByGameCount(1);
        Log.e("INFO", "size of scoresAdapter : " + sizeOfGame);

        String[] values = new String[sizeOfGame];
        if(sizeOfGame > 0){
            for(int i = 1; i <= sizeOfGame; i++){
                values[i-1] = scoresAdapter.getFirstThrow(1,i) + " " + scoresAdapter.getSecondThrow(1,i) + " " + scoresAdapter.getThirdThrow(1,i);
            }

        }else Message.message(getActivity(), "ScoreDatabase empty");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
