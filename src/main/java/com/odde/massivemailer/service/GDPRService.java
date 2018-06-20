package com.odde.massivemailer.service;

import com.odde.massivemailer.exception.EmailException;
import com.odde.massivemailer.model.ContactPerson;
import com.odde.massivemailer.model.Mail;
import edu.emory.mathcs.backport.java.util.Collections;

public class GDPRService {

    private MailService mailService;
    private TemplateService templateService;

    public GDPRService(MailService mailService, TemplateService templateService) {
        this.mailService = mailService;
        this.templateService = templateService;
    }

    public void sendConsentRequestEmail() {
        ContactPerson.getContactsWithoutConsentRequest().stream()
                .map(this::makeConsentRequestMail)
                .forEach(this::sendConsentRequestEmail);
    }

    private void sendConsentRequestEmail(Mail mail) {
        try {
            mailService.send(mail);
        } catch (EmailException e) {
            throw new RuntimeException("Cannot send email to " + mail.getReceipts(), e);
        }
    }

    private Mail makeConsentRequestMail(ContactPerson contactPerson) {
        String content = templateService.createConsentEmailContent(Collections.emptyMap());
        return Mail.createConsentRequestEmail(content, contactPerson.getEmail());
    }
}
