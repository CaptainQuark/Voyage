package com.example.thomas.voyage.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassicWorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassicWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassicWorkoutFragment extends Fragment {

    private int
            throwCounter = 0,
            numRoundTotal=-1,
            roundNow=-1,
            numGoalPoints=-1,
            goalPoinsNowNegative=-1;
    private List<Integer> undoList = new ArrayList<>();
    private List<TextView> hitViewList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private View rootView;
    private TextView hitOneView, hitTwoView, hitThreeView, pointsLeftView;

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
            goalPoinsNowNegative = numGoalPoints;
        }
        else
        {
            Message.message(getActivity(), "ERROR  getArguments in Fragment");
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

        hitViewList.add(hitOneView);
        hitViewList.add(hitTwoView);
        hitViewList.add(hitThreeView);

        for(int i = 0; i < hitViewList.size(); i++){
            hitViewList.get(i).setTextColor(Color.LTGRAY);
        }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setOneThrow(int points){
        goalPoinsNowNegative -= points;
        undoList.add(points);

        if(goalPoinsNowNegative < 0){
            goalPoinsNowNegative = numGoalPoints;
            Message.message(getActivity(), "One more round finished...");
        }

        pointsLeftView.setText(Integer.toString(goalPoinsNowNegative));

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
    }


    public class SimpleNumberAdapter extends BaseAdapter {
        private Context mContext;
        private List<Integer> selectionList = new ArrayList<>();

        public SimpleNumberAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return 50;
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

}
