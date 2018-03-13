package com.example.ianwa.distfr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateAccActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
    }

    public void onCreateAccClicked(View view){
        String acc_id = "";
        TextView t = (TextView) findViewById(R.id.createAcc_message);
        final String acc_name = ((EditText) findViewById(R.id.createAcc_name)).getText().toString();
        final String acc_user = ((EditText) findViewById(R.id.createAcc_user)).getText().toString();
        final String acc_pass1 = ((EditText) findViewById(R.id.createAcc_pass1)).getText().toString();
        String acc_pass2 = ((EditText) findViewById(R.id.createAcc_pass2)).getText().toString();
        if (acc_name.isEmpty() || acc_user.isEmpty() || acc_pass1.isEmpty() || acc_pass2.isEmpty()) {
            t.setText(getString(R.string.error_blank));
            return;
        }
        if (!acc_pass1.equals(acc_pass2)) {
            t.setText(getString(R.string.error_pass));
            return;
        }

        // server connection
        try {
            Thread tr = new Thread(new Runnable() {
                @Override
                public void run() {
                    TextView x = (TextView) findViewById(R.id.createAcc_hidden);
                    x.setText(ServerTalk.comm(formatCreateAcc(acc_name, acc_user, acc_pass1)));
                }
            });
            tr.start();
            tr.join();
            TextView x2 = (TextView) findViewById(R.id.createAcc_hidden);
            String response = x2.getText().toString();
            System.out.println(response);

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        Intent intent = new Intent(this, AccountHomeActivity.class);
        intent.putExtra("acc_id", acc_id);
        startActivity(intent);
        finish();
    }

    public void onBackClicked(View view){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public HashMap<String, String> formatCreateAcc(String name, String user, String pass){
        HashMap<String, String> res = new HashMap<>();
        res.put("cmd", "create_acc");
        res.put("acc_name", name);
        res.put("acc_user", user);
        res.put("acc_pass", pass);

        return res;
    }
}
