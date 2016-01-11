package com.bqclientevernote.afrasilv.asyntask;

import android.os.AsyncTask;

import com.bqclientevernote.afrasilv.casobqclienteevernote.MainActivity;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

/**
 * Created by alex on 11/01/16.
 */
public class GetNoteMetada extends AsyncTask<Notebook, Void, Note> {
    private String guid;
    Note notesWithContent;
    MainActivity activity;

    public GetNoteMetada(String _guid, MainActivity _activity) {
        this.guid = _guid;
        activity = _activity;
    }


    @Override
    protected Note doInBackground(Notebook... params) {
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        try {
            notesWithContent = noteStoreClient.getNote(this.guid, true, false, false, false);
        } catch (EDAMUserException e) {
            e.printStackTrace();
        } catch (EDAMSystemException e) {
            e.printStackTrace();
        } catch (EDAMNotFoundException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        };


        return this.notesWithContent;
    }

    @Override
    protected void onPostExecute(Note notesWithContent) {
        this.activity.addNote(notesWithContent);
    }
}
