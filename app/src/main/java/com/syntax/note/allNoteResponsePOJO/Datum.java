package com.syntax.note.allNoteResponsePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("catId")
    @Expose
    private String catId;
    @SerializedName("catName")
    @Expose
    private String catName;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("noteList")
    @Expose
    private List<NoteList> noteList = null;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public List<NoteList> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<NoteList> noteList) {
        this.noteList = noteList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
