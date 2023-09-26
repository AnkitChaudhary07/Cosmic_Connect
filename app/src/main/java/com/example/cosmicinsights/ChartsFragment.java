package com.example.cosmicinsights;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ChartsFragment extends Fragment {

   SearchView search;
   TextView all_text, family_text, friends_text, celebrity_text;

    public ChartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charts, container, false);
        search = view.findViewById(R.id.search);
        all_text = view.findViewById(R.id.all_text);
        family_text = view.findViewById(R.id.family_text);
        friends_text = view.findViewById(R.id.friends_text);
        celebrity_text = view.findViewById(R.id.celebrity_text);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        all_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        family_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        friends_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });

        celebrity_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Sorry..No functionality added", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}