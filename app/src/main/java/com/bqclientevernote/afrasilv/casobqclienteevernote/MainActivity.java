package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bqclientevernote.afrasilv.asyntask.GetNoteMetada;
import com.bqclientevernote.afrasilv.fragments.NoteFragment;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EvernoteSession mEvernoteSession;
    private static final String CONSUMER_KEY = "aalex12-8143";
    private static final String CONSUMER_SECRET = "7f51f235db3bfe04";

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = false;

    private ArrayList<Note> noteList = new ArrayList<>();
    private int listNotesSize;

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

        final MainActivity mainActivity = this;

        noteStoreClient.listNotebooksAsync(new EvernoteCallback<List<Notebook>>() {
            @Override
            public void onSuccess(List<Notebook> result) {
                for (Notebook notebook : result) {
                    NoteFilter filter = new NoteFilter();
                    filter.setNotebookGuid(notebook.getGuid());

                    final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

                    noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
                        @Override
                        public void onSuccess(NoteList noteList) {
                            List<Note> notes = noteList.getNotes();

                            setSize(notes.size());

                            for (Note note : notes) {
                                GetNoteMetada getNoteMetada = new GetNoteMetada(note.getGuid(), mainActivity);
                                getNoteMetada.execute();
                            }

                        }

                        @Override
                        public void onException(Exception exception) {
                            Log.e("ERROR", "Error retrieving notebooks", exception);
                        }

                    });

                }


            }



            @Override
            public void onException(Exception exception) {
                Log.e("ERROR", "Error retrieving notebooks", exception);
            }
        });



    }

    public void addNote(Note note){
        this.noteList.add(note);
        if(this.listNotesSize == this.noteList.size()){
            FragmentManager fmgr = getFragmentManager();
            NoteFragment fragment = (NoteFragment) fmgr.findFragmentById(R.id.note_fragment);
            fragment.setNotes(noteList);
        }
    }

    public void setSize(int size){
        this.listNotesSize = size;
    }

}
