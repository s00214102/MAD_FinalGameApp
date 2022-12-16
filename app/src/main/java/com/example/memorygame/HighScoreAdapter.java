package com.example.memorygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoreAdapter extends ArrayAdapter<HighScore> {
    public HighScoreAdapter(Context context, ArrayList<HighScore> highscores) {
        super(context, 0, highscores);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HighScore highScore = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_highscore, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvScore);
        // Populate the data into the template view using the data object
        tvName.setText(highScore._name);
        tvHome.setText(highScore._score);
        // Return the completed view to render on screen
        return convertView;
    }
}
