package com.utcn.socialplatform.service;

import org.springframework.stereotype.Component;

@Component
public interface MailService {
    public void sendEmail(String toEmail, String subject, String body);
}