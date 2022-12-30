package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "attachment_id")
public class AttachmentId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AttachmentFile attachmentFile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttachmentFile getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(AttachmentFile attachmentFile) {
        this.attachmentFile = attachmentFile;
    }
}
