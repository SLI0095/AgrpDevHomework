package com.example.homeworkagrpdev.Entities;

import java.sql.Timestamp;
import java.util.UUID;


public class DocumentRecord {

    private long id;
    private UUID documentUuid;
    private UUID userUuid;
    private Timestamp openedOn;

    public DocumentRecord(long id, UUID documentUuid, UUID userUuid, Timestamp openedOn) {
        this.id = id;
        this.documentUuid = documentUuid;
        this.userUuid = userUuid;
        this.openedOn = openedOn;
    }

    public DocumentRecord(UUID documentUuid, UUID userUuid) {
        this.documentUuid = documentUuid;
        this.userUuid = userUuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(UUID documentUuid) {
        this.documentUuid = documentUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Timestamp getOpenedOn() {
        return openedOn;
    }

    public void setOpenedOn(Timestamp openedOn) {
        this.openedOn = openedOn;
    }
}
