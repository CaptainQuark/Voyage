package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.thomas.voyage.Message;
import com.example.thomas.voyage.R;

import java.util.ArrayList;
import java.util.List;

public class ClassicWorkoutFragment extends Fragment {

    private int
            throwCounter = 0,
            numRoundTotal= -1,
            roundNow= -1,
            numGoalPoints= -1,
            goalPointsNow = -1,
            lastUsedScoreField = -1,
            lastUsedMulti = -1;
    private boolean saveToStats = true;
    private PrefsHandler prefsHandler;
    private List<Integer> undoList = new ArrayList<>();
    private List<TextView> hitViewList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private TextView hitOneView, hitTwoView, hitThreeView, pointsLeftView, playUntilView;

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
        else
        {
            Message.message(getActivity(), "ERROR  getArguments in Fragment");
        }

        if(getActivity() != null){

            // SharedPreferences hier initialiseren und an Konstruktor des Containers übergeben,
            // Initialisierung nur mit 'getActivity' im Fragment möglich, nicht in innerer Klasse
            SharedPreferences prefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            prefsHandler = new PrefsHandler(prefs, editor);

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

        pointsLeftView.setText(Integer.toString(goalPointsNow));

        hitViewList.add(hitOneView);
        hitViewList.add(hitTwoView);
        hitViewList.add(hitThreeView);

        for(int i = 0; i < hitViewList.size(); i++){
            hitViewList.get(i).setTextColor(Color.LTGRAY);
        }

        playUntilView.setText(roundNow + " / " + numRoundTotal);

        GridView cricketView = (GridView) rootView.findViewById(R.id.classic_workout_gridview);
        cricketView.setAdapter(new SimpleNumberAdapter(getActivity()));
        cricketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // handle onItemClick-Action
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void setOneThrow(int initialValue, int multi){
        goalPointsNow -= (initialValue * multi);
        undoList.add(initialValue * multi);

        if(saveToStats){
            if(multi != 0 && initialValue != 25 && initialValue != 50){
                lastUsedMulti = multi;
                prefsHandler.setValues(("X" + multi), initialValue);
            }

            lastUsedScoreField = initialValue;
            prefsHandler.setValues("SCORE_FIELD_AMOUNT", initialValue);
        }


        if(goalPointsNow < 0){
            goalPointsNow = numGoalPoints;
            Message.message(getActivity(), "One more round finished...");
            roundNow++;

            if(roundNow > numRoundTotal){
                Message.message(getActivity(), "You have finished the session!");
                if (mListener != null) {
                    mListener.putFragmentToSleep();
                }
            }else{
                playUntilView.setText(roundNow + " / " + numRoundTotal);
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
        if( (throwCounter % 3) == 0){
            for( int i = 0; i < hitViewList.size(); i++){
                hitViewList.get(i).setTextColor(Color.LTGRAY);
            }
        }
    }

    public void undoLastThrow(){
        if(!undoList.isEmpty()){
            prefsHandler.undoLastChange(lastUsedScoreField, lastUsedMulti);
            goalPointsNow += undoList.get( undoList.size() - 1 );
            pointsLeftView.setText(Integer.toString(goalPointsNow));

            throwCounter--;
            hitViewList.get( (throwCounter % 3) ).setTextColor(Color.LTGRAY);
            hitViewList.get( throwCounter % 3 ).setText(undoList.get(undoList.size() - 1) + "");

            undoList.remove(undoList.size() - 1);
        }
        else{
            Message.message(getActivity(), "No Actions to undo");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        prefsHandler.saveValuesToPreferences();
        mListener = null;
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
        public void onFragmentInteraction(Uri uri);
        public void putFragmentToSleep();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

            return convertView;
        }
    }

    private class PrefsHandler{
        private SharedPreferences.Editor editor;

        // speichern Anzahl der Treffer pro Zahl, nicht deren summierter Wert
        private static final String SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ = "SCORE_FIELD_AMOUNT_WITHOUT_MULTI_",
                NUM_OF_MULTI_ONE_BY_VALUE = "NUM_OF_MULTI_ONE_BY_VALUE_",
                NUM_OF_MULTI_TWO_BY_VALUE = "NUM_OF_MULTI_ONE_BY_VALUE_",
                NUM_OF_MULTI_THREE_BY_VALUE = "NUM_OF_MULTI_ONE_BY_VALUE_";
        private List<Long> totalAmountByFieldValueList,
                totalNumOfMultiOneByValueList,
                totalNumOfMultiTwoByValueList,
                totalNumOfMultiThreeByValueList;

        public PrefsHandler(SharedPreferences prefs, SharedPreferences.Editor tempEditor){
            String TEMP_PREF_VAL_ID = "", TEMP_PREF_MULTI_ONE = "", TEMP_PREF_MULTI_TWO = "", TEMP_PREF_MULTI_THREE = "";
            totalNumOfMultiOneByValueList = new ArrayList<>();
            totalNumOfMultiTwoByValueList = new ArrayList<>();
            totalNumOfMultiThreeByValueList = new ArrayList<>();
            totalAmountByFieldValueList = new ArrayList<>();

            editor = tempEditor;

            // '0' == Anzahl an missed throws
            for(int i = 0; i <= 22; i++){
                if(i < 21){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + i;
                }else if(i == 21){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + 25;
                }else if(i == 22){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + 50;
                }

                totalAmountByFieldValueList.add(prefs.getLong(TEMP_PREF_VAL_ID, 0));
            }

            for(int i = 1; i <= 20; i++){
                TEMP_PREF_MULTI_ONE = NUM_OF_MULTI_ONE_BY_VALUE + i;
                TEMP_PREF_MULTI_TWO = NUM_OF_MULTI_TWO_BY_VALUE + i;
                TEMP_PREF_MULTI_THREE = NUM_OF_MULTI_THREE_BY_VALUE + i;

                totalNumOfMultiOneByValueList.add(prefs.getLong(TEMP_PREF_MULTI_ONE, 0));
                totalNumOfMultiTwoByValueList.add(prefs.getLong(TEMP_PREF_MULTI_TWO, 0));
                totalNumOfMultiThreeByValueList.add(prefs.getLong(TEMP_PREF_MULTI_THREE, 0));
            }
        }

        public long getValues(String id, long val){
            long value = -1;
            int index = -1;

            switch (id){
                case "X1":
                    index = (int) val;
                    value = totalNumOfMultiOneByValueList.get(index) + 1;
                    break;
                case "X2":
                    index = (int) val;
                    value = totalNumOfMultiTwoByValueList.get(index) + 1;
                    break;
                case "X3":
                    index = (int) val;
                    value = totalNumOfMultiThreeByValueList.get(index) + 1;
                    break;
                case "SCORE_FIELD_AMOUNT":
                    index = (int) val;
                    value = totalAmountByFieldValueList.get(index) + 1;
                    break;
                default:
                    Log.e("ERROR","DEFAULT @ ClassicWorkoutFragment : setValue");
            }

            return value;
        }

        public void setValues(String id, long val){
            int index = (int) val;

            switch (id){
                case "X1":
                    totalNumOfMultiOneByValueList.set(index-1, totalNumOfMultiOneByValueList.get(index-1) + 1);
                    Message.message(getActivity(), totalNumOfMultiOneByValueList.size() + " : X1");
                    break;
                case "X2":
                    totalNumOfMultiTwoByValueList.set(index-1, totalNumOfMultiTwoByValueList.get(index-1) + 1);
                    Message.message(getActivity(), totalNumOfMultiTwoByValueList.size() + " : X2");
                    break;
                case "X3":
                    totalNumOfMultiThreeByValueList.set(index-1, totalNumOfMultiThreeByValueList.get(index-1) + 1);
                    Message.message(getActivity(), totalNumOfMultiThreeByValueList.size() + " : X3");
                    break;
                case "SCORE_FIELD_AMOUNT":
                    Message.message(getActivity(),totalAmountByFieldValueList.size() + " : SCORE");
                    if(index != 25 && index != 50){
                        totalAmountByFieldValueList.set(index, totalAmountByFieldValueList.get(index) + 1);
                    }else if(index == 25){
                        totalAmountByFieldValueList.set(21, totalAmountByFieldValueList.get(21) + 1);
                    }else if(index == 50){
                        totalAmountByFieldValueList.set(22, totalAmountByFieldValueList.get(22) + 1);
                    }

                    break;
                default:
                    Log.e("ERROR","DEFAULT @ ClassicWorkoutFragment : setValue");
            }
        }

        public void undoLastChange(int val, int multi){

            totalAmountByFieldValueList.set(val, totalAmountByFieldValueList.get(val) - 1);

            switch (multi){
                case 1:
                    totalNumOfMultiOneByValueList.set(multi, totalNumOfMultiOneByValueList.get(multi) - 1);
                    break;
                case 2:
                    totalNumOfMultiTwoByValueList.set(multi, totalNumOfMultiTwoByValueList.get(multi) - 1);
                    break;
                case 3:
                    totalNumOfMultiThreeByValueList.set(multi, totalNumOfMultiThreeByValueList.get(multi) - 1);
                    break;
                default:
                    Log.e("ERROR", "DEFAULT @ ClassicWorkoutFragment : undoLastChange in PrefHandler.class");
            }
        }

        public void saveValuesToPreferences(){
            String TEMP_PREF_VAL_ID = "", TEMP_PREF_MULTI_ONE = "", TEMP_PREF_MULTI_TWO = "", TEMP_PREF_MULTI_THREE = "";

            // TODO: 23-Aug-15 save values to 'editor.'
            // editor.putLong(NUM_OF_MULTI_ONE_BY_VALUE, totalNumOfMultiOneByValueList.get(index));
            // editor.apply();

            // '0' == Anzahl an missed throws
            for(int i = 0; i <= 22; i++){
                if(i < 21){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + i;
                }else if(i == 21){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + 25;
                }else if(i == 22){
                    TEMP_PREF_VAL_ID = SCORE_FIELD_AMOUNT_WITHOUT_MULTI_ + 50;
                }

                editor.putLong(TEMP_PREF_VAL_ID, totalAmountByFieldValueList.get(i));
            }

            // In Schleife zur Initialisieurng wird zwar von 1 - 20 initialisiert, jedoch mit '1' der 1. Eintrag zur
            // List hinzugefügt, welcher als Index '0' hat
            for(int i = 0; i < 20; i++){
                TEMP_PREF_MULTI_ONE = NUM_OF_MULTI_ONE_BY_VALUE + i;
                TEMP_PREF_MULTI_TWO = NUM_OF_MULTI_TWO_BY_VALUE + i;
                TEMP_PREF_MULTI_THREE = NUM_OF_MULTI_THREE_BY_VALUE + i;

                editor.putLong(TEMP_PREF_MULTI_ONE, totalNumOfMultiOneByValueList.get(i));
                editor.putLong(TEMP_PREF_MULTI_TWO, totalNumOfMultiTwoByValueList.get(i));
                editor.putLong(TEMP_PREF_MULTI_THREE, totalNumOfMultiThreeByValueList.get(i));
            }

            editor.apply();
        }
    }
}
