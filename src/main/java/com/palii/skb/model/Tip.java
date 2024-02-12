package com.palii.skb.model;

public class Tip {
    private String body;
    private String title;
    private String createdDate;
    private String editedDate;
    private int useCount;
    private int id;


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tip(String title, String body, int useCount) {
        this.title = title;
        this.body = body;
        this.useCount = useCount;
    }

    public Tip(String title, String body, int id, int useCount, String editDate, String createDate) {
        this.title = title;
        this.body = body;
        this.id = id;
        this.useCount = useCount;
        this.createdDate = createDate;
        this.editedDate = editDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(String editedDate) {
        this.editedDate = editedDate;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
