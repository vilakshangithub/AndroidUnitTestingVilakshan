package com.android.vilakshansaxena.androidunittesting.keynotes.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.vilakshansaxena.androidunittesting.R;
import com.android.vilakshansaxena.androidunittesting.keynotes.AddKeyNotePresenter;
import com.android.vilakshansaxena.androidunittesting.keynotes.AddKeyNoteProtocol;
import com.android.vilakshansaxena.androidunittesting.keynotes.KeyNote;
import com.android.vilakshansaxena.androidunittesting.keynotes.adapter.NoteRecyclerAdapter;

import java.util.List;

public class AddNoteActivity extends AppCompatActivity implements AddKeyNoteProtocol.View {

    AddKeyNoteProtocol.Presenter presenter;
    private RecyclerView noteListRecycler;
    private View emptyView;
    private NoteRecyclerAdapter noteRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        presenter = new AddKeyNotePresenter(this, null);
        emptyView = findViewById(R.id.empty_text);
        noteListRecycler = (RecyclerView) findViewById(R.id.recycler_notes);
        noteListRecycler.setLayoutManager(new LinearLayoutManager(this));
        noteRecyclerAdapter = new NoteRecyclerAdapter(this);
        noteListRecycler.setAdapter(noteRecyclerAdapter);
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showNoteList(List<KeyNote> keyNoteList) {
        noteRecyclerAdapter.setData(keyNoteList);
        noteListRecycler.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyList() {
        emptyView.setVisibility(View.VISIBLE);
        noteListRecycler.setVisibility(View.GONE);
    }
}
