package com.odde.massivemailer.model;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.Message;
import javax.mail.Session;

import com.odde.massivemailer.service.impl.SqliteContact;
import org.junit.Test;
import java.net.InetAddress;

public class MailTest {
	
	@Test
	public void testCreateMessage() throws Exception {

		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props);
		Mail mail = new Mail();
		mail.setContent("content");
		mail.setSubject("subject");
		mail.setReceipts(Arrays.asList("test@gmail.com"));
		
		List<Message> messages = mail.createMessages(session);
		
		assertEquals(1, messages.size());
		assertEquals(mail.getSubject(), messages.get(0).getSubject());
	}

	@Test
	public void testCreateMessageWithTemplate() throws Exception {
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props);

		SqliteContact mockSqliteContact = mock(SqliteContact.class);
		ContactPerson contactPerson = new ContactPerson();
		contactPerson.setName("TestName");
		contactPerson.setLastname("LastName");
		contactPerson.setEmail("EmailName");
		contactPerson.setCompany("CompanyName");
		when(mockSqliteContact.getContactByEmail(anyString())).thenReturn(contactPerson);

		Mail mail = new Mail(mockSqliteContact);
		mail.setMessageId(System.currentTimeMillis());
		mail.setContent("content {FirstName}, {Company}");
		mail.setSubject("subject {LastName} - {Email}");
		mail.setReceipts(Arrays.asList("test@gmail.com"));

		List<Message> messages = mail.createMessages(session);


		verify(mockSqliteContact).getContactByEmail(anyString());
		assertEquals("<html><body>content TestName, CompanyName<img src=\"http://"+ InetAddress.getLocalHost().getHostAddress()+":8070/massive_mailer/resources/images/qrcode.png?messageId=" + mail.getMessageId() + "&userId="+"test@gmail.com"+"\">Test.gif</img></body></html>", messages.get(0).getContent());
		assertEquals("subject LastName - EmailName", messages.get(0).getSubject());
	}
	
	@Test
	public void testDisplayName() throws Exception {

		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props);
		
		Mail mail = new Mail();
		mail.setContent("content");
		mail.setSubject("subject");
		mail.setReceipts(Arrays.asList("test@gmail.com"));
		
		List<Message> messages = mail.createMessages(session);
		String[] address = messages.get(0).getFrom()[0].toString().split("<");
		
		assertEquals("Inspector Gadget", address[0].trim());
	}

	@Test
	public void AsNotification() {
		Mail mail = new Mail();
		mail.setContent("content");
		mail.setSubject("subject");
		mail.setMessageId(123456789L);

		Notification notification = mail.asNotification();

		assertThat(notification.getSubject(), is("subject"));
		assertThat(notification.getNotificationId(), is(123456789L));
	}
}
