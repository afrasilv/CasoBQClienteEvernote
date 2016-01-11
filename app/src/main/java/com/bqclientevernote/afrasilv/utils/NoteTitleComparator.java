package com.bqclientevernote.afrasilv.utils;

import com.evernote.edam.type.Note;

import java.util.Comparator;

public class NoteTitleComparator implements Comparator<Note> {

        @Override
        public int compare(Note lhs, Note rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    }