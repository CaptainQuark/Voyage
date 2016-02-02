package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Msg;
import com.example.thomas.voyage.Databases.DBscorefieldAndMultiAmountAdapter;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class ClassicVersusFragment extends Fragment implements View.OnClickListener {

    private DBscorefieldAndMultiAmountAdapter scoreHelper;
    private OnVersusInteractionListener mListener;
    private List<Integer> undoList = new ArrayList<>();
    private List<TextView> scoreFieldList = new ArrayList<>();
    private List<PlayerDataHolder> playerHolderList = new ArrayList<>();
    private List<MultiValKeyHistory> multiValKeyHistoryList = new ArrayList<>();
    private ImageView activeOneView, activeTwoView;
    private TextView showStatsView, hideStatsView;
    private GridView statsGridView;
    private int activePlayer = 0, numLegsToWin = -1, throwCount = 0, pointsToFinishLeg = -1;
    private boolean restrictionReached = false;
    private static final int NUM_THROWS_PER_ROUND = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scoreHelper = new DBscorefieldAndMultiAmountAdapter(getActivity());

        if (getArguments() != null) {
            Bundle args = getArguments();
            numLegsToWin = args.getInt("NUM_ROUND_TOTAL");
            pointsToFinishLeg = args.getInt("NUM_GOAL_POINTS");
        }
        else{
            Msg.msg(getActivity(), "ERROR  getArguments in Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_classic_versus_fragment, container, false);

        for(int i = 0; i < 3; i++) scoreFieldList.add(
                (TextView) rootView.findViewById(getActivity().getResources().getIdentifier("versus_score_field_" + (i + 1), "id", getActivity().getPackageName())));

        for(int i = 0; i < 2; i++) playerHolderList.add( new PlayerDataHolder(i, pointsToFinishLeg, rootView));

        activeOneView = (ImageView) rootView.findViewById(R.id.shanghai_active_player_1);
        activeTwoView = (ImageView) rootView.findViewById(R.id.shanghai_active_player_2);
        showStatsView = (TextView) rootView.findViewById(R.id.quick_classic_versus_textview_show_stats);
        hideStatsView = (TextView) rootView.findViewById(R.id.quick_classic_versus_hide_stats);
        showStatsView.setOnClickListener(this);
        hideStatsView.setOnClickListener(this);

        statsGridView = (GridView) rootView.findViewById(R.id.classic_versus_gridview);
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
            case R.id.quick_classic_versus_textview_show_stats:
                showStatsView.setVisibility(View.GONE);
                statsGridView.setVisibility(View.VISIBLE);
                hideStatsView.setVisibility(View.VISIBLE);
                break;

            case R.id.quick_classic_versus_hide_stats:
                showStatsView.setVisibility(View.VISIBLE);
                statsGridView.setVisibility(View.GONE);
                hideStatsView.setVisibility(View.GONE);
                break;

            default:
                Msg.msg(getActivity(), "DEFAULT @ onClick");
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


        throwCount++;
        if(throwCount == 1)  for(int i = 0; i < 3; i++) scoreFieldList.get(i).setTextColor(Color.LTGRAY);

        playerHolderList.get(activePlayer).pointsByLegNow -= (val * multi);
        multiValKeyHistoryList.add( new MultiValKeyHistory(val, multi));
        undoList.add(val * multi);
        if(restrictionReached) restrictionReached = false;

        playerHolderList.get(activePlayer).playerViewsList.get(2).setText( playerHolderList.get(activePlayer).pointsByLegNow + "");
        scoreFieldList.get(throwCount-1).setText(Integer.toString( val * multi) );
        scoreFieldList.get(throwCount-1).setTextColor(Color.BLACK);

        if(playerHolderList.get(activePlayer).pointsByLegNow <= 0){
            // Wenn leg abgeschlossen wurde

            for(int i = 0; i < 3; i++) scoreFieldList.get(i).setText("-");
            for(int i = 0; i < 2; i++){
                playerHolderList.get(i).resetPointValues();
            }

            playerHolderList.get(activePlayer).legCount++;
            playerHolderList.get(activePlayer).playerViewsList.get(1).setText( playerHolderList.get(activePlayer).legCount + "");

            if(playerHolderList.get(activePlayer).legCount == numLegsToWin){
                Msg.msg(getActivity(), "Du Gewinner");
            }
        }

        if(throwCount == NUM_THROWS_PER_ROUND){

            if(activePlayer == 0){
                activePlayer = 1;
                activeOneView.setBackgroundColor(Color.BLACK);
                activeTwoView.setBackgroundResource(R.color.quick_combat_player_2);

            }else{
                activePlayer = 0;
                activeOneView.setBackgroundResource(R.color.quick_combat_player_1);
                activeTwoView.setBackgroundColor(Color.BLACK);
            }

            throwCount = 0;
        }
    }

    public void undoLastThrow(){
        if(!undoList.isEmpty() && !restrictionReached){
            int tempVal;
            int initialValue = multiValKeyHistoryList.get(multiValKeyHistoryList.size() - 1).getMulti();

            /*
            switch (multiValKeyHistoryList.get(multiValKeyHistoryList.size() - 1).getMulti()){
                case 1:
                    tempVal = scoreHelper.getMulitplierOne(initialValue);
                    tempVal--;
                    scoreHelper.updateX1(initialValue, tempVal);
                    break;

                case 2:
                    tempVal = scoreHelper.getMultiplierTwo(initialValue);
                    tempVal--;
                    scoreHelper.updateX2(initialValue, tempVal);
                    break;

                case 3:
                    tempVal = scoreHelper.getMulitplierThree(initialValue);
                    tempVal--;
                    scoreHelper.updateX3(initialValue, tempVal);
                    break;

                default:
                    Msg.msg(getActivity(), "DEFAULT @ setOneThrow : multi");
                    break;
            }*/

            multiValKeyHistoryList.remove( multiValKeyHistoryList.size() -1 );

            throwCount--;

            if(throwCount < 0){
                //Msg.msg(getActivity(), "activePlayer : " + activePlayer);
                throwCount = 2;
                if(activePlayer == 0) activePlayer = 1;
                else activePlayer = 0;
            }
            if(activePlayer == 0){
                playerHolderList.get(activePlayer).isActiveView.setBackgroundResource(R.color.quick_combat_player_1);
                playerHolderList.get(1).isActiveView.setBackgroundColor(Color.BLACK);
            }
            else {
                playerHolderList.get(activePlayer).isActiveView.setBackgroundResource(R.color.quick_combat_player_2);
                playerHolderList.get(0).isActiveView.setBackgroundColor(Color.BLACK);
            }

            int tempPoints = playerHolderList.get(activePlayer).pointsByLegNow + undoList.get( undoList.size() - 1 );
            playerHolderList.get(activePlayer).pointsByLegNow = tempPoints;

            if(tempPoints == 501){
                //Msg.msg(getActivity(), "activePlayer hast 501");

                int tempPointsOtherPlayer;

                if( activePlayer == 0) tempPointsOtherPlayer = playerHolderList.get(1).pointsByLegNow;
                else tempPointsOtherPlayer = playerHolderList.get(0).pointsByLegNow;

                if(tempPointsOtherPlayer == 501){
                    restrictionReached = true;
                    //Msg.msg(getActivity(), "restriction reached : " + restrictionReached);

                    if(throwCount != 0) Msg.msg(getActivity(), "Undo restricted to one leg maximum.");

                }
            }

            playerHolderList.get(activePlayer).playerViewsList.get(2).setText(Integer.toString(playerHolderList.get(activePlayer).pointsByLegNow));

            scoreFieldList.get( (throwCount % 3) ).setTextColor(Color.parseColor("#FF969696"));
            scoreFieldList.get( throwCount % 3 ).setText(undoList.get(undoList.size() - 1) + "");

            undoList.remove(undoList.size() - 1);
        }
        else{
            Msg.msg(getActivity(), "No more actions to undo");
            mListener.dismissRecordButtons(true);
        }

        statsGridView.invalidateViews();
    }

    private class MultiValKeyHistory {
        private int val, multi;

        public MultiValKeyHistory(int valTemp, int multiTemp){
            val = valTemp;
            multi = multiTemp;
        }

        public int getVal(){ return val; }

        public int getMulti(){ return multi; }
    }

    private class PlayerDataHolder{

        private String playerName = "Willi von Wal- und Haselnuss";
        private int legCount = 0, pointsByLegNow = -1, pointsByLegThreshold = -1;
        List<TextView> playerViewsList;
        ImageView isActiveView;

        public PlayerDataHolder(int id, int pointsThreshold, View rootView){
            /*
            playerViewsList indizes:

                        0 = Name (TextView)
                        1 = Legs (TextView)
                        2 = fehlende Punkte in diesem Leg (TextView)
             */

            if(id == 1) playerName = "Krieger Kraftstark";

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

            pointsByLegThreshold = pointsThreshold;
            resetPointValues();
        }

        public void resetPointValues(){
            pointsByLegNow = pointsByLegThreshold;
             playerViewsList.get(2).setText( pointsByLegThreshold + "");
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
