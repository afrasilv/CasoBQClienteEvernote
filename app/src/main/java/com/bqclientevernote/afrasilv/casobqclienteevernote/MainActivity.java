package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bqclientevernote.afrasilv.fragments.NotebookFragment;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Notebook;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NotebookFragment.OnFragmentInteractionListener {

    EvernoteSession mEvernoteSession;
    private static final String CONSUMER_KEY = "aalex12-8143";
    private static final String CONSUMER_SECRET = "7f51f235db3bfe04";

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginToEvernote();
        if (! mEvernoteSession.isLoggedIn()) {
            mEvernoteSession.authenticate(this);
            finish();
            return;
        }


        loadNotes();

    }


    private void loginToEvernote() {
        String consumerKey;
        if ("Your consumer key".equals(CONSUMER_KEY)) {
            consumerKey = BuildConfig.EVERNOTE_CONSUMER_KEY;
        } else {
            // isn't the default value anymore
            consumerKey = CONSUMER_KEY;
        }

        String consumerSecret;
        if ("Your consumer secret".equals(CONSUMER_SECRET)) {
            consumerSecret = BuildConfig.EVERNOTE_CONSUMER_SECRET;
        } else {
            // isn't the default value anymore
            consumerSecret = CONSUMER_SECRET;
        }

        mEvernoteSession = new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .build(consumerKey, consumerSecret)
                .asSingleton();
    }

    private void loadNotes(){
        EvernoteNoteStoreClient noteStoreClient = mEvernoteSession.getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                List<String> namesList = new ArrayList<>(result.size());
                for (Notebook notebook : result) {
                    namesList.add(notebook.getName());
                }
                String notebookNames = TextUtils.join(", ", namesList);
                Toast.makeText(getApplicationContext(), notebookNames + " notebooks have been retrieved", Toast.LENGTH_LONG).show();


                FragmentManager fmgr = getFragmentManager();
                NotebookFragment fragment = (NotebookFragment) fmgr.findFragmentById(R.id.notebook_list);
                fragment.setNotebooks(result);
            }

            @Override
            public void onException(Exception exception) {
                Log.e("ERROR", "Error retrieving notebooks", exception);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Notebook notebook) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("notebook", notebook);
        startActivity(intent);
    }
}
