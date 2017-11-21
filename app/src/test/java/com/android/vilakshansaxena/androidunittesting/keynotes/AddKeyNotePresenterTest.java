package com.android.vilakshansaxena.androidunittesting.keynotes;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddKeyNotePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    public AddKeyNoteProtocol.View view;

    @Mock
    public KeyNoteAPI keyNoteAPI;


    @Test
    public void testAddKeyNotesBasic() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        KeyNote keyNote = new KeyNote();
        presenter.addNotes(keyNote);

        verify(view).showLoader();
        verify(keyNoteAPI).addKeyNotes(keyNote);
        verify(view).hideLoader();
    }

    @Test
    public void testAddKeyNotesSuccess() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        KeyNote keyNote = new KeyNote();
        when(presenter.handleAddNoteApi(keyNote)).thenReturn(new KeyNote());
        presenter.addNotes(keyNote);

        verify(view).showSuccess();
    }

    @Test
    public void testAddKeyNotesFailure() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        KeyNote keyNote = new KeyNote();
        when(presenter.handleAddNoteApi(keyNote)).thenReturn(null);
        presenter.addNotes(keyNote);

        verify(view).showError();
    }

    @Test
    public void testGetKeyNotesBasic() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        presenter.getAllNotes();

        verify(view).showLoader();
        verify(keyNoteAPI).getKeyNotes();
        verify(view).hideLoader();
    }

    @Test
    public void testGetKeyNotesWithData() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        List<KeyNote> keyNoteList = getKeyNoteList();
        when(presenter.handleGetNoteApi()).thenReturn(keyNoteList);
        presenter.getAllNotes();

        verify(view).showNoteList(keyNoteList);
    }

    @Test
    public void testGetKeyNotesWithNullData() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        when(presenter.handleGetNoteApi()).thenReturn(null);
        presenter.getAllNotes();

        verify(view).showEmptyList();
    }

    @Test
    public void testGetKeyNotesWithEmptyData() {
        AddKeyNoteProtocol.Presenter presenter = new AddKeyNotePresenter(view, keyNoteAPI);

        when(presenter.handleGetNoteApi()).thenReturn((List<KeyNote>) anyList());
        presenter.getAllNotes();

        verify(view).showEmptyList();
    }

    private List<KeyNote> getKeyNoteList() {
        List<KeyNote> keyNoteList = new ArrayList<>();
        KeyNote keyNote = new KeyNote();
        keyNote.title = "Key Note 1";
        keyNoteList.add(keyNote);
        keyNote = new KeyNote();
        keyNote.title = "Key Note 2";
        keyNoteList.add(keyNote);
        keyNote = new KeyNote();
        keyNote.title = "Key Note 3";
        keyNoteList.add(keyNote);
        keyNote = new KeyNote();
        keyNote.title = "Key Note 4";
        keyNoteList.add(keyNote);
        keyNote = new KeyNote();
        keyNote.title = "Key Note 5";
        keyNoteList.add(keyNote);
        keyNote = new KeyNote();
        keyNote.title = "Key Note 6";
        keyNoteList.add(keyNote);
        return keyNoteList;
    }

}