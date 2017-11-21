package com.android.vilakshansaxena.androidunittesting.keynotes;

import java.util.List;

import javax.inject.Inject;

public class AddKeyNotePresenter implements AddKeyNoteProtocol.Presenter {

    private AddKeyNoteProtocol.View view;

    @Inject
    public KeyNoteAPI keyNoteAPI;

    public AddKeyNotePresenter(AddKeyNoteProtocol.View view, KeyNoteAPI keyNoteAPI) {
        this.view = view;
        this.keyNoteAPI = keyNoteAPI;
    }

    @Override
    public void addNotes(KeyNote keyNote) {
        view.showLoader();
        KeyNote finalKeyNote = handleAddNoteApi(keyNote);
        view.hideLoader();
        if(finalKeyNote != null){
            view.showSuccess();
        } else {
            view.showError();
        }
    }

    @Override
    public KeyNote handleAddNoteApi(KeyNote keyNote) {
        return keyNoteAPI.addKeyNotes(keyNote);
    }

    @Override
    public void getAllNotes() {
        view.showLoader();
        List<KeyNote> keyNoteList = handleGetNoteApi();
        if(keyNoteList != null && keyNoteList.size() > 0){
            view.showNoteList(keyNoteList);
        } else {
            view.showEmptyList();
        }
        view.hideLoader();
    }

    @Override
    public List<KeyNote> handleGetNoteApi() {
        return keyNoteAPI.getKeyNotes();
    }


}
