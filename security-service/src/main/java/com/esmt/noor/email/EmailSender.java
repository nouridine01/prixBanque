package com.esmt.noor.email;

public interface EmailSender {
    void send(String to, String email);
    String buildEmail(String name, String link);
    String buildLink(String token);
}
