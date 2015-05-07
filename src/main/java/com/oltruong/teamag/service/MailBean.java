package com.oltruong.teamag.service;

import com.google.common.collect.Lists;

import java.util.List;

public class MailBean {

    private String subject;

    private String content;

    private List<String> recipientList = Lists.newArrayList();

    private List<String> blindRecipientList = Lists.newArrayList();

    public MailBean() {
    }


    public void addRecipient(String recipient) {
        recipientList.add(recipient);
    }

    public List<String> getRecipientList() {
        return recipientList;
    }

    public void addBlindRecipient(String recipient) {
        blindRecipientList.add(recipient);
    }

    public List<String> getBlindRecipientList() {
        return blindRecipientList;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
