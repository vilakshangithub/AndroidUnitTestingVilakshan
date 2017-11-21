package com.android.vilakshansaxena.androidunittesting.keynotes;

import java.util.List;

public class AddKeyNoteProtocol {
    public interface View {
        void showLoader();
        void showSuccess();
        void hideLoader();
        void showError();
        void showNoteList(List<KeyNote> keyNoteList);
        void showEmptyList();
    }

    public interface Presenter {
        void addNotes(KeyNote keyNote);
        KeyNote handleAddNoteApi(KeyNote keyNote);
        void getAllNotes();
        List<KeyNote> handleGetNoteApi();
    }
}
