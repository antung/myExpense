package com.antang.myexpense.db.entity;

import com.antang.myexpense.model.Tags;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "tags", primaryKeys = {"tagId", "tagName"},
        indices = {@Index(value = "tagId", unique = true),
                @Index(value = "tagName", unique = true)})
public class TagEntity implements Tags {
    private int tagId;
    @NonNull
    private String tagName;

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }


    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public int getTagId() {
        return tagId;
    }

    @Override
    public String getTagName() {
        return tagName;
    }
}
