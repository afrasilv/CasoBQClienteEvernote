package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;

/**
 * Created by alex on 7/01/16.
 */
public class DashBoard extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    EvernoteSession mEvernoteSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        mEvernoteSession = EvernoteSession.getInstance();

        Button mLogOutButton = (Button) findViewById(R.id.close_session);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEvernoteSession.logOut();
                finish();
            }
        });
    }

    @Override
    public void onLoginFinished(boolean successful) {
        // handle result

        Toast.makeText(DashBoard.this, "Login finished!", Toast.LENGTH_SHORT).show();
    }
}
