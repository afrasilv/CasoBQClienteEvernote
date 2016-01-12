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
public class EditNoteAsyntask extends AsyncTask<Void, Void, Void> {
    private Note note;

    public EditNoteAsyntask(Note _note) {
        this.note = _note;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient().updateNote(this.note);
        } catch (EDAMUserException e) {
            e.printStackTrace();
        } catch (EDAMSystemException e) {
            e.printStackTrace();
        } catch (EDAMNotFoundException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        };

        return null;
    }
}
