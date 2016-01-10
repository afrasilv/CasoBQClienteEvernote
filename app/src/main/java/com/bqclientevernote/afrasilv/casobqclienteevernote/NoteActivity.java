package com.bqclientevernote.afrasilv.casobqclienteevernote;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bqclientevernote.afrasilv.asyntask.GetNoteMetada;
import com.bqclientevernote.afrasilv.fragments.NoteFragment;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.type.Notebook;

import java.util.concurrent.ExecutionException;


public class NoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Notebook notebook = (Notebook) getIntent().getSerializableExtra("notebook");

        GetNoteMetada getNoteMetada = new GetNoteMetada(this, notebook);
        getNoteMetada.execute();


        NotesMetadataList notesMetadataList = null;
        try {
            notesMetadataList = getNoteMetada.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        };

        FragmentManager fmgr = getFragmentManager();
        NoteFragment fragment = (NoteFragment) fmgr.findFragmentById(R.id.note_fragment);
        fragment.setNotes(notesMetadataList.getNotes());


        /*
        noteStoreClient.findNotesAsync(filter, 0, 100, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(NoteList noteList) {
                FragmentManager fmgr = getFragmentManager();
                NoteFragment fragment = (NoteFragment) fmgr.findFragmentById(R.id.note_fragment);
                fragment.setNotes(noteList.getNotes());

            }

            @Override
            public void onException(Exception e) {
                Toast.makeText(NoteActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


}