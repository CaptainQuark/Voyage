package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBghostMetaDataAdapter;
import com.example.thomas.voyage.Databases.DBghostScoreDataAdapter;
import com.example.thomas.voyage.R;


public class ClassicHistoricalFragment extends Fragment implements View.OnClickListener{

    private DBghostScoreDataAdapter scoresAdapter;
    private DBghostMetaDataAdapter metaDataAdapter;
    private Context context = null;
    private onHistoricalInteractionListener mListener;
    private FrameLayout selectionView;
    private LinearLayout gameView;
    private ImageView backButton;
    private int numGoalPoints = 301;

    public ClassicHistoricalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scoresAdapter = new DBghostScoreDataAdapter(getActivity());
        metaDataAdapter = new DBghostMetaDataAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classic_historical, container, false);

        mListener.dismissRecordButtons(false);
        mListener.dismissScoreButtonBar(false);

        selectionView = (FrameLayout) rootView.findViewById(R.id.historical_framelayout_selection);
        gameView = (LinearLayout) rootView.findViewById(R.id.historical_linearlayout_game);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_historical);

        int numGames = (int) metaDataAdapter.getTaskCount();
        String[] values = new String[numGames];

        for(int i = 1; i <= numGames; i++){
            values[i-1] = metaDataAdapter.getOneRow(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Msg.msg(getActivity(), "Item selected!");
                mListener.dismissScoreButtonBar(true);
                mListener.dismissRecordButtons(true);
                selectionView.setVisibility(View.GONE);
                gameView.setVisibility(View.VISIBLE);
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onHistoricalInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVersusInteractionListener");
        }

        context = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        context = null;
    }

    @Override
    public void onClick(View view) {

    }

    public interface onHistoricalInteractionListener {
        void dismissRecordButtons(boolean setVisible);
        void dismissScoreButtonBar(boolean setVisible);
        void putFragmentToSleep();
        boolean getSaveStatsChoice();
    }
}










        /*
        if(sizeOfGame > 0){
            for(int i = 1; i <= sizeOfGame; i++){
                values[i-1] = scoresAdapter.getFirstThrow(1,i) + " " + scoresAdapter.getSecondThrow(1,i) + " " + scoresAdapter.getThirdThrow(1,i);
            }

        }else Msg.msg(getActivity(), "ScoreDatabase empty");
        */
