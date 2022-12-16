package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        int score = getIntent().getIntExtra("score",-1);
        TextView tvScore = findViewById(R.id.tvFinalScore);
        tvScore.setText(String.valueOf(score));
    }

    public void doPlayAgain(View view) {
        finish();
    }
}