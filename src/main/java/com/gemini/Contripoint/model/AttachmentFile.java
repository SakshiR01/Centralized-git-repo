package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "attachment_file")
public class AttachmentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int hashcode;
    private byte[] file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
