package com.example.thomas.voyage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.voyage.R;

public class ScreenSlidePageFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        TextView pager = (TextView) rootView.findViewById(R.id.pager);


        int index = getArguments().getInt("INDEX", 0);
        /*
        Uri imagePath = Uri.parse("R.mipmap.journey_b" + index);

        pager.setImageURI(imagePath);
        */

        switch (index) {
            case 0:
                rootView.setBackgroundResource(R.mipmap.journey_b0);
                break;
            case 1:
                rootView.setBackgroundResource(R.mipmap.journey_b1);
                break;
            case 2:
                rootView.setBackgroundResource(R.mipmap.journey_b2);
                break;
            default:
                break;
        }

        return rootView;
    }
}
