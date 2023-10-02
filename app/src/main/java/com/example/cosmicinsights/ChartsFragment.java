package com.example.cosmicinsights;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ChartsFragment extends Fragment {

   SearchView search;
   TextView all_text, family_text, friends_text, celebrity_text;

   ImageView relatives_btn, family_btn, celebrity_btn, others_btn;

    public ChartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        search = view.findViewById(R.id.search);
        relatives_btn = view.findViewById(R.id.relatives_btn);
        family_btn = view.findViewById(R.id.family_btn);
        celebrity_btn = view.findViewById(R.id.celebrity_btn);
        others_btn = view.findViewById(R.id.others_btn);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        relatives_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        family_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        celebrity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        others_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}