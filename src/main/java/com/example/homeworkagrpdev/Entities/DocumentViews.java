package com.example.homeworkagrpdev.Entities;

import java.util.UUID;

public class DocumentViews {

    private UUID documentUuid;
    private int views;

    public DocumentViews(UUID documentUuid, int views) {
        this.documentUuid = documentUuid;
        this.views = views;
    }

    public UUID getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(UUID documentUuid) {
        this.documentUuid = documentUuid;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
