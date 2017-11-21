package com.android.vilakshansaxena.androidunittesting.keynotes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.keynotes.KeyNote;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created by vilakshansaxena on 9/14/17.
 * </p>
 */

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {

    private Context context;
    private List<KeyNote> keyNoteList;

    public NoteRecyclerAdapter(Context context) {
        this.context = context;
        keyNoteList = new ArrayList<>();
    }

    public void setData(List<KeyNote> keyNoteList) {
        this.keyNoteList.clear();
        this.keyNoteList.addAll(keyNoteList);
        notifyDataSetChanged();
    }

    @Override
    public NoteRecyclerAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View noteLineItemView = LayoutInflater.from(context).
                inflate(R.layout.item_note_list, parent, false);
        return new NoteRecyclerAdapter.NoteViewHolder(noteLineItemView);
    }

    @Override
    public void onBindViewHolder(NoteRecyclerAdapter.NoteViewHolder holder, int position) {
        KeyNote keyNote = keyNoteList.get(position);
        holder.attributeName.setText(keyNote.title);
    }

    @Override
    public int getItemCount() {
        return keyNoteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView attributeName;

        NoteViewHolder(View itemView) {
            super(itemView);
            attributeName = (TextView) itemView.findViewById(R.id.itemName);
        }
    }

}

