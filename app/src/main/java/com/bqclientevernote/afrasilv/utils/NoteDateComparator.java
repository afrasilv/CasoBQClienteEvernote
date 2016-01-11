package com.bqclientevernote.afrasilv.utils;

import com.evernote.edam.type.Note;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by alex on 12/01/16.
 */
public class NoteDateComparator implements Comparator<Note> {

    @Override
    public int compare(Note lhs, Note rhs) {
        return new Date(lhs.getCreated()).compareTo(new Date(rhs.getCreated()));
    }
}
