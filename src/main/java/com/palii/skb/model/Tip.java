package com.example.knowledge_db.model;

import java.util.Date;

public class Tip {

    private String text;
    private Date createdDate;
    private Date editedDate;
    private Integer useCount;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Tip(String text, Date createdDate, Date editedDate, Integer useCount, Integer id) {
        this.text = text;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.useCount = useCount;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }


}
