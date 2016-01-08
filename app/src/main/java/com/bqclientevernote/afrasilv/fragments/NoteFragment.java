package com.bqclientevernote.afrasilv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.bqclientevernote.afrasilv.casobqclienteevernote.R;
import com.evernote.edam.type.Note;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class NoteFragment extends Fragment {

    private ListAdapter mAdapter;
    private AbsListView mListView;

    public NoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter<NoteWrapped>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        mListView.setOnItemClickListener(this);

        return view;
    }

    public void setNotes(List<Note> notes) {
        ArrayAdapter<NoteWrapped> adapter = (ArrayAdapter<NoteWrapped>) mAdapter;
        for (Note note : notes) {
            adapter.add(new NoteWrapped(note));
        }
    }

    private class NoteWrapped {
        private final String title;

        public NoteWrapped(Note note) {
            this.title = note.getTitle();
        }

        @Override
        public String toString() {
            return title;
        }
    }
}