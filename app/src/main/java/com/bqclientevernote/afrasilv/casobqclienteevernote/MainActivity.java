package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;


public class MainActivity extends AppCompatActivity {

    EvernoteSession mEvernoteSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEvernoteSession = EvernoteSession.getInstance();

        if(mEvernoteSession.isLoggedIn()){
            Intent intent = new Intent().setClass(
                    MainActivity.this, DashBoard.class);
            startActivity(intent);
            finish();
        }

        if(!mEvernoteSession.isLoggedIn()){
            mEvernoteSession.authenticate(this);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* switch(requestCode) {
            case EvernoteSession.REQUEST_CODE_LOGIN:*/
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "toast 2!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent().setClass(
                            MainActivity.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                }
     /*           break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }*/
    }

}
