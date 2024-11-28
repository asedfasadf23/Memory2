package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TilesView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.view);
    }

    public void onNewGameClick(View v) {

        if (view != null)
        {
            view.newGame(); // запустить игру заново
            Toast.makeText(this, "New game started", Toast.LENGTH_SHORT).show();
        }
        // very useful comment

    }
}