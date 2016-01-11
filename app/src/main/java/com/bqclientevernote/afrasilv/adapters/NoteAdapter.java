package com.bqclientevernote.afrasilv.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bqclientevernote.afrasilv.casobqclienteevernote.R;
import com.evernote.edam.type.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 10/01/16.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Note> lista = new ArrayList<>();
    private Activity activity;
    private String shareText;
    private int lastPosition = -1;


    public NoteAdapter(Context context, ArrayList<Note> lista, Activity activity) {
        this.context = context;
        this.lista = lista;
        this.activity = activity;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameNegTextView;
        public CardView mCard;

        public ViewHolder(View v) {
            super(v);
            mNameNegTextView = (TextView) v.findViewById(R.id.nota_name);
            mCard = (CardView) v.findViewById(R.id.card_note);
        }
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_note, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lista.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mNameNegTextView.setText(lista.get(position).getTitle());

        setAnimation(holder.mCard, position);


        String content = lista.get(position).getContent();

        Document doc = Jsoup.parse(content);

        final String finalContent = doc.getElementsByTag("div").toString();

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(activity)
                        .title(lista.get(position).getTitle())
                        .titleGravity(GravityEnum.CENTER)
                        .content(finalContent)
                        .positiveText(R.string.edit_note)
                        .negativeText(R.string.close)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {

                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();



                dialog.show();
            }
        });
    }

    public void updateNotes(ArrayList<Note> noteList) {
        this.lista = noteList;
        notifyDataSetChanged();
    }
}
