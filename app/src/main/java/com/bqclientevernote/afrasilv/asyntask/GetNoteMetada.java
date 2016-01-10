package com.bqclientevernote.afrasilv.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

import java.util.List;

/**
 * Created by alex on 11/01/16.
 */
public class GetNoteMetada extends AsyncTask<Notebook, Void, NotesMetadataList> {
    private Notebook notebook;
    private NotesMetadataList notesMetadataList;

    public GetNoteMetada(Context context, Notebook _notebook) {
        notebook = _notebook;
    }




    @Override
    protected NotesMetadataList doInBackground(Notebook... params) {
        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        NotesMetadataResultSpec resultSpec = new NotesMetadataResultSpec();
        resultSpec.setIncludeTitle(true);

        NoteFilter filter = new NoteFilter();
        filter.setNotebookGuid(notebook.getGuid());


        List<NoteMetadata> noteList = null;
        int offset = 0;
        notesMetadataList = null;
        do {
            try {
                notesMetadataList = noteStoreClient.findNotesMetadata(filter, offset, 100, resultSpec);
            } catch (EDAMUserException e1) {
                e1.printStackTrace();
            } catch (EDAMSystemException e1) {
                e1.printStackTrace();
            } catch (EDAMNotFoundException e1) {
                e1.printStackTrace();
            } catch (TException e1) {
                e1.printStackTrace();
            }
            noteList.addAll(notesMetadataList.getNotes());
            offset = offset + notesMetadataList.getNotesSize();
        } while (notesMetadataList != null && offset < notesMetadataList.getTotalNotes());

        return notesMetadataList;
    }

}
