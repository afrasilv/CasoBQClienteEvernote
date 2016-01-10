package com.bqclientevernote.afrasilv.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bqclientevernote.afrasilv.adapters.NoteAdapter;
import com.bqclientevernote.afrasilv.casobqclienteevernote.R;
import com.evernote.edam.notestore.NoteMetadata;
import com.evernote.edam.type.Note;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class NoteFragment extends Fragment {

    private ArrayList<NoteMetadata> noteList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public NoteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    public void setNotes(List<NoteMetadata> notes) {

        if((noteList == null)||(!noteList.isEmpty()))
            noteList = new ArrayList<>();

        for (NoteMetadata note : notes) {
            noteList.add(note);
        }

        mAdapter = new NoteAdapter(getActivity().getBaseContext(), noteList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
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