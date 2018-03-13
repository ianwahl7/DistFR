package com.example.ianwa.distfr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    public void onLoginClicked(View view) throws Exception{
        String acc_id = "";
        TextView t = (TextView) findViewById(R.id.login_message);
        final String acc_user = ((EditText) findViewById(R.id.login_user)).getText().toString();
        final String acc_pass = ((EditText) findViewById(R.id.login_pass)).getText().toString();
        if (acc_user.isEmpty() || acc_pass.isEmpty()) {
            t.setText(getString(R.string.error_blank));
            return;
        }

        // server connection
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                TextView x = (TextView) findViewById(R.id.login_hidden);
                x.setText(ServerTalk.comm(formatLogin(acc_user, acc_pass)));
            }
        });
        tr.start();
        tr.join();
        TextView x2 = (TextView) findViewById(R.id.login_hidden);
        String response = x2.getText().toString();
        System.out.println(response);

        //TODO

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

    public HashMap<String, String> formatLogin(String user, String pass){
        HashMap<String, String> res = new HashMap<>();
        res.put("cmd", "login");
        res.put("acc_user", user);
        res.put("acc_pass", pass);
        return res;
    }
}
