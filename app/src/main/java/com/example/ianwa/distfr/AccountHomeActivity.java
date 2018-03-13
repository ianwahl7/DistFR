package com.example.ianwa.distfr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AccountHomeActivity extends AppCompatActivity {

    String acc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        acc_id = getIntent().getStringExtra("acc_id");
    }

    public void onRecognizeClicked(View view){
        Intent intent= new Intent(this, RecognizeActivity.class);
        intent.putExtra("acc_id", acc_id);
        startActivity(intent);
        finish();
    }

    public void onAddFaceClicked(View view){
        Intent intent= new Intent(this, AddFaceActivity.class);
        intent.putExtra("acc_id", acc_id);
        startActivity(intent);
        finish();
    }

    public void onLogoutClicked(View view){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
