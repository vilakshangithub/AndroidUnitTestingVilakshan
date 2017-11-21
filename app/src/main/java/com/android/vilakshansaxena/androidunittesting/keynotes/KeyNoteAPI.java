package com.android.vilakshansaxena.androidunittesting.keynotes;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;

public interface KeyNoteAPI {

    @POST("/submit/keynote")
    KeyNote addKeyNotes(KeyNote keyNote);

    @GET("/get/keynote")
    List<KeyNote> getKeyNotes();
}
