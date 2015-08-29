package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.example.thomas.voyage.ContainerClasses.Message;
import com.example.thomas.voyage.Databases.DBghostMetaDataAdapter;
import com.example.thomas.voyage.Databases.DBghostScoreDataAdapter;
import com.example.thomas.voyage.Databases.DBscorefieldAndMultiAmountAdapter;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassicWorkoutFragment extends Fragment implements View.OnClickListener{

    private int
            throwCounter = 0,
            numRoundTotal= -1,
            roundNow= -1,
            numGoalPoints= -1,
            goalPointsNow = -1,
            lastUsedScoreField = -1,
            lastUsedMulti = -1, FINISH_FACTOR = 2;
    private boolean saveToStats = true, saveGhost = false;

    // MultiValKeyHistory-Liste hat *keine* Verwendung - später vielleicht als Cache einbauen, um nicht ständig Read-/Write auf Datenbank auszuführen
    private List<MultiValKeyHistory> multiValKeyHistoryList;
    private List<Integer> undoList = new ArrayList<>();
    private List<TextView> hitViewList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private GridView statsGridView;
    private TextView hitOneView, hitTwoView, hitThreeView, pointsLeftView, playUntilView, showStatsView, hideStatsView, noStatsRecordingView, activateGhostRecordView;
    private FrameLayout frameStatsView;
    private DBscorefieldAndMultiAmountAdapter scoreHelper;

    public static ClassicWorkoutFragment newInstance(String param1, String param2) {
        //ClassicWorkoutFragment fragment = new ClassicWorkoutFragment();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return new ClassicWorkoutFragment();
    }

    public ClassicWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle args = getArguments();
            numRoundTotal = args.getInt("NUM_ROUND_TOTAL");
            numGoalPoints = args.getInt("NUM_GOAL_POINTS");
            goalPointsNow = numGoalPoints;
            roundNow = 1;
        }
        else{
            Message.message(getActivity(), "ERROR  getArguments in Fragment");
        }

        if(getActivity() != null){
            scoreHelper = new DBscorefieldAndMultiAmountAdapter(this.getActivity());
            multiValKeyHistoryList = new ArrayList<>();

        }else{
            Message.message(getActivity(), "ERROR @ getActivity in Fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_classic_workout, container, false);

        hitOneView = (TextView) rootView.findViewById(R.id.workout_first_hit);
        hitTwoView = (TextView) rootView.findViewById(R.id.workout_second_hit);
        hitThreeView = (TextView) rootView.findViewById(R.id.workout_third_hit);
        pointsLeftView = (TextView) rootView.findViewById(R.id.workout_points_left_to_hit);
        playUntilView = (TextView) rootView.findViewById(R.id.workout_round_time_view);
        showStatsView = (TextView) rootView.findViewById(R.id.quick_classic_textview_show_stats);
        frameStatsView = (FrameLayout) rootView.findViewById(R.id.classic_workout_framelayout_stats_in_detail);
        hideStatsView = (TextView) rootView.findViewById(R.id.quick_classic_textview_hide_stats);

        frameStatsView.setOnClickListener(this);
        hideStatsView.setOnClickListener(this);

        pointsLeftView.setText(Integer.toString(goalPointsNow));

        hitViewList.add(hitOneView);
        hitViewList.add(hitTwoView);
        hitViewList.add(hitThreeView);

        for(int i = 0; i < hitViewList.size(); i++){
            hitViewList.get(i).setTextColor(Color.parseColor("#FF969696"));
        }

        if(roundNow < numRoundTotal) playUntilView.setText(roundNow + " / " + numRoundTotal);
        else playUntilView.setText("LETZTE RUNDE");

        statsGridView = (GridView) rootView.findViewById(R.id.classic_workout_gridview);
        statsGridView.setAdapter(new SimpleNumberAdapter(getActivity()));
        statsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // handle onItemClick-Action
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.classic_workout_framelayout_stats_in_detail:
                showStatsView.setVisibility(View.GONE);
                statsGridView.setVisibility(View.VISIBLE);
                break;

            case R.id.quick_classic_textview_hide_stats:
                showStatsView.setVisibility(View.VISIBLE);
                statsGridView.setVisibility(View.GONE);
                break;
        }
    }

    public void recordGhostData(boolean b){
        saveGhost = b;
    }

    public void setOneThrow(int initialValue, int multi){

        if(goalPointsNow == numGoalPoints) {
            saveToStats = mListener.getSaveStatsChoice();
        }

        //List<Integer> arrayList = new ArrayList<>();
        mListener.dismissRecordButtons(false);
        lastUsedScoreField = initialValue;
        goalPointsNow -= (initialValue * multi);
        undoList.add(initialValue * multi);
        multiValKeyHistoryList.add(new MultiValKeyHistory(initialValue, multi));

        if(saveToStats){
            if(multi != 0){
                int tempVal;

                switch (multi){
                    case 1:
                        tempVal = scoreHelper.getMulitplierOne(initialValue);
                        tempVal++;
                        scoreHelper.updateX1(initialValue, tempVal);
                        break;

                    case 2:
                        tempVal = scoreHelper.getMultiplierTwo(initialValue);
                        tempVal++;
                        scoreHelper.updateX2(initialValue, tempVal);
                        break;

                    case 3:
                        tempVal = scoreHelper.getMulitplierThree(initialValue);
                        tempVal++;
                        scoreHelper.updateX3(initialValue, tempVal);
                        break;

                    default:
                        Message.message(getActivity(), "DEFAULT @ setOneThrow : multi");
                        break;
                }

                //Message.message(getActivity(), "Database size: " + scoreHelper.getTaskCount());
            }
        }

        // Auf Ende der Session überprüfen
        if( goalPointsNow < 0 ){

                goalPointsNow = numGoalPoints;
                Message.message(getActivity(), "One more round finished...");
                roundNow++;

                if(roundNow > numRoundTotal){
                    Message.message(getActivity(), "You have finished the session!");
                    if (mListener != null) {
                        mListener.putFragmentToSleep();
                    }
                }else{
                    if(roundNow < numRoundTotal) playUntilView.setText(roundNow + " / " + numRoundTotal);
                    else playUntilView.setText("LETZTE RUNDE");
                }
        }

        pointsLeftView.setText(Integer.toString(goalPointsNow));

        if(!hitViewList.isEmpty()){
            hitViewList.get(throwCounter % 3).setText(Integer.toString(undoList.get(throwCounter)));
            hitViewList.get(throwCounter % 3).setTextColor(Color.BLACK);
        }
        else{
            Message.message(getActivity(), "ERROR @ setOneThrow : hitViewList is empty!");
        }

        throwCounter++;

         /*
        if(initialValue > 0 && (initialValue*multi) <= 170 && (throwCounter % 3 == 0)){
            CheckoutRes check = new CheckoutRes();
            int[] array = check.getArrayOfCheckoutVals(goalPointsNow);
            if(array[0] != -1){
                for (int i = 0; i < array.length; i++){

                    if(i == 0){
                        hitOneView.setText(array[i] + "");
                        hitOneView.setTextColor(Color.RED);
                    }
                    else if(i == 1){
                        hitTwoView.setText(array[i] + "");
                        hitTwoView.setTextColor(Color.RED);
                    }
                    else if(i == 2){
                        hitThreeView.setText(array[i] + "");
                        hitThreeView.setTextColor(Color.RED);
                    }
                }
            }

        }
        */

        if( (throwCounter % 3) == 0){

                for( int i = 0; i < hitViewList.size(); i++){
                    hitViewList.get(i).setTextColor(Color.parseColor("#FF969696"));
                }
        }

        statsGridView.invalidateViews();
    }

    public void undoLastThrow(){
        if(!undoList.isEmpty()){
            int tempVal;
            int initialValue = multiValKeyHistoryList.get(multiValKeyHistoryList.size() - 1).getMulti();

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
                    Message.message(getActivity(), "DEFAULT @ setOneThrow : multi");
                    break;
            }

            multiValKeyHistoryList.remove( multiValKeyHistoryList.size() -1 );

            goalPointsNow += undoList.get( undoList.size() - 1 );
            pointsLeftView.setText(Integer.toString(goalPointsNow));

            throwCounter--;
            hitViewList.get( (throwCounter % 3) ).setTextColor(Color.parseColor("#FF969696"));
            hitViewList.get( throwCounter % 3 ).setText(undoList.get(undoList.size() - 1) + "");

            undoList.remove(undoList.size() - 1);
        }
        else{
            Message.message(getActivity(), "No Actions to undo");
            mListener.dismissRecordButtons(true);
        }

        statsGridView.invalidateViews();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVersusInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Message.message(getActivity(), "onDetach");

        if(saveGhost){

            //Message.message(getActivity(), "saveGhost called");
            DBghostScoreDataAdapter db = new DBghostScoreDataAdapter(getActivity());
            DBghostMetaDataAdapter dbGhost = new DBghostMetaDataAdapter(getActivity());

            int undoSize = undoList.size();

            for(int i = 0; i < undoSize; i++){
                if( undoSize - i >= 3) db.insertData(1, i, undoList.get(i++), undoList.get(i++), undoList.get(i++));
                else if( undoSize - i >= 2) db.insertData(1, i, undoList.get(i++), undoList.get(i++),-1);
                else if( undoSize - i >= 1) db.insertData(1, i, undoList.get(i++),-1, -1);

            }

            // Hier werden zugehörige Informationen in eigene Datenbank eingetragen (Vermeidung von Overhead durch leere Spalen in obiger Tabelle)
            // Bis auf Name alles automatisch
            float avg = 0;
            for(int i = 0; i < undoSize; i++){
                avg += undoList.get(i);
            }

            Message.message(getActivity(), "Avg RAW: " + avg);

            avg /= undoSize;
            Message.message(getActivity(), "Avg real: " + avg);

            Date date = new Date();
            dbGhost.insertData((int) (dbGhost.getTaskCount() + 1), "Bobby", undoSize, avg, date.toString());

            long count = db.getRowsByGameCount(1);
            Message.message(getActivity(), "rowCount : " + count);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void dismissRecordButtons(boolean setVisible);
        void putFragmentToSleep();
        boolean getSaveStatsChoice();
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

    private class MultiValKeyHistory {
        private int val, multi;

        public MultiValKeyHistory(int valTemp, int multiTemp){
            val = valTemp;
            multi = multiTemp;
        }

        public int getVal(){ return val; }

        public int getMulti(){ return multi; }
    }
}
