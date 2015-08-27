package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBscorefieldAndMultiAmountAdapter;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class ClassicVersusFragment extends Fragment implements View.OnClickListener {

    private DBscorefieldAndMultiAmountAdapter scoreHelper;
    private OnVersusInteractionListener mListener;
    private List<TextView> scoreFieldList = new ArrayList<>();
    private List<PlayerDataHolder> playerHolderList = new ArrayList<>();
    private int activePlayer = 0, numLegsToWin = 9, throwCount = 1;
    private static final int NUM_THROWS_PER_ROUND = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scoreHelper = new DBscorefieldAndMultiAmountAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classic_versus_fragment, container, false);

        for(int i = 0; i < 3; i++) scoreFieldList.add(
                (TextView) rootView.findViewById(getActivity().getResources().getIdentifier("versus_score_field_" + (i + 1), "id", getActivity().getPackageName())));

        for(int i = 0; i < 2; i++) playerHolderList.add( new PlayerDataHolder(i, 501, rootView));

        GridView statsGridView = (GridView) rootView.findViewById(R.id.classic_versus_gridview);
        statsGridView.setAdapter(new SimpleNumberAdapter(getActivity()));
        statsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // handle onItemClick-Action
            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            default:
                Message.message(getActivity(), "DEFAULT @ onClick");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnVersusInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVersusInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void handleThrow(int val, int multi){

        if( playerHolderList.get(activePlayer).legCount == 0 &&  playerHolderList.get(activePlayer).pointsByLegNow == playerHolderList.get(activePlayer).pointsByLegThreshold){
            mListener.dismissRecordButtons(false);
        }

        if(throwCount <= NUM_THROWS_PER_ROUND){

            playerHolderList.get(activePlayer).pointsByLegNow -= (val * multi);
            playerHolderList.get(activePlayer).playerViewsList.get(2).setText( playerHolderList.get(activePlayer).pointsByLegNow + "");
            scoreFieldList.get(throwCount-1).setText(Integer.toString( val * multi) );

            throwCount++;

            if(playerHolderList.get(activePlayer).pointsByLegNow <= 0){
                // Wenn leg abgeschlossen wurde

                for(int i = 0; i < 3; i++) scoreFieldList.get(i).setText("-");

                playerHolderList.get(activePlayer).legCount++;
                playerHolderList.get(activePlayer).playerViewsList.get(1).setText( playerHolderList.get(activePlayer).legCount + "");

                if(playerHolderList.get(activePlayer).legCount == playerHolderList.get(activePlayer).pointsByLegThreshold){
                    Message.message(getActivity(), "Du Gewinner");
                }
            }

        }else{
            for(int i = 0; i < 3; i++) scoreFieldList.get(i).setText("-");

            if(activePlayer == 0){
                activePlayer = 1;
            }else{
                activePlayer = 0;
            }

            throwCount = 1;
        }


        playerHolderList.get(activePlayer).playerViewsList.get(0).setText( playerHolderList.get(activePlayer).pointsByLegNow + "");

    }

    private class PlayerDataHolder{

        private String playerName = "Willi von Wal- und Haselnuss";
        private int legCount = 0, pointsByLegNow = -1, pointsByLegThreshold = -1;
        List<TextView> playerViewsList;
        ImageView isActiveView;

        public PlayerDataHolder(int id, int pointsThreshold, View rootView){
            pointsByLegThreshold = pointsThreshold;
            resetPointValues();

            /*
            playerViewsList indizes:

                        0 = Name (TextView)
                        1 = Legs (TextView)
                        2 = fehlende Punkte in diesem Leg (TextView)
             */

            playerViewsList = new ArrayList<>();

            playerViewsList.add(
                    (TextView) rootView.findViewById(
                            getActivity().getResources().getIdentifier("quick_shanghai_player_name_" + (id+1), "id", getActivity().getPackageName())) );

            playerViewsList.add(
                    (TextView) rootView.findViewById(
                            getActivity().getResources().getIdentifier("classic_shanghai_leg_count_view_" + (id+1), "id", getActivity().getPackageName())) );

            playerViewsList.add(
                    (TextView) rootView.findViewById(
                            getActivity().getResources().getIdentifier("classic_shanghai_points_player_" + (id+1), "id", getActivity().getPackageName())) );

            isActiveView = (ImageView) rootView.findViewById(getActivity().getResources().getIdentifier("shanghai_active_player_" + (id+1), "id", getActivity().getPackageName()));

            playerViewsList.get(0).setText(playerName);
        }

        public void resetPointValues(){
            pointsByLegNow = pointsByLegThreshold;
        }
    }












    public interface OnVersusInteractionListener {
        public void dismissRecordButtons(boolean setVisible);
        public void putFragmentToSleep();
        public boolean getSaveStatsChoice();
    }

    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return 12;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            TextView dataView, titleView;

            ViewHolder(View v) {

                dataView = (TextView) v.findViewById(R.id.fragment_classic_workout_card_data);
                titleView = (TextView) v.findViewById(R.id.fragment_classic_workout_card_title);
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.quick_workout_card, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            switch (position){

                case 0:
                    holder.titleView.setText("singles 5");
                    holder.dataView.setText(scoreHelper.getMulitplierOne(5) + "");
                    break;
                case 1:
                    holder.titleView.setText("doubles 5");
                    holder.dataView.setText(Integer.toString(scoreHelper.getMultiplierTwo(5)));
                    break;
            }

            return convertView;
        }
    }
}
