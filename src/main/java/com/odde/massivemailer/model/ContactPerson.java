package com.odde.massivemailer.model;

import com.odde.massivemailer.model.validator.UniquenessValidator;
import com.odde.massivemailer.service.LocationProviderService;
import com.odde.massivemailer.service.exception.GeoServiceException;
import com.odde.massivemailer.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.annotations.Table;

import java.time.LocalDate;
import java.util.*;


@Table("contact_people")
public class ContactPerson extends ApplicationModel {
    static {
        validatePresenceOf("email");
        validateWith(new UniquenessValidator("email"));
    }

    public static final String FIRSTNAME = "FirstName";
    public static final String LASTNAME = "LastName";
    public static final String EMAIL = "Email";
    public static final String COMPANY = "Company";
    public static final String LOCATION = "Location";
    public static final String COURSES_SENT = "courses_sent";
    public static final String DATE_SENT = "date_sent";
    public static final String LONGITUDE = "Longitude";
    public static final String LATITUDE = "Latitude";
    public static final String CONSENT_REQUEST_DATE = "consent_request_date";
    public static final String CONSENT_RECEIVED_DATE = "consent_received_date";
    public static final String CONSENT_SENT = "consent_sent";
    public static final String CONSENT_RECEIVED = "consent_received";


    public Double getLatitude() {
        return getDoubleAttribute(LATITUDE);
    }
    public Double getLongitude() {
        return getDoubleAttribute(LONGITUDE);
    }

    public Map<String, String> attributes = new HashMap<>();

    public ContactPerson() { }

    public ContactPerson(String name, String email, String lastname) {
        this(name, email, lastname, "");
    }

    public ContactPerson(String name, String email, String lastname, String company) {
        setName(name);
        setEmail(email);
        setLastname(lastname);
        setCompany(company);
    }


    public ContactPerson(String name, String email, String lastname, String company,String location) {
        setName(name);
        setEmail(email);
        setLastname(lastname);
        setCompany(company);
        setLocation(location);
        if (!StringUtils.isEmpty(getLocation())) {
            LocationProviderService locationProviderService = new LocationProviderService();
            try {
                Location locationDetails = locationProviderService.getLocationForName(getLocation());

                if (locationDetails != null) {
                    set(LATITUDE, locationDetails.getLat());
                    set(LONGITUDE, locationDetails.getLng());
                }
            } catch (GeoServiceException e) {
                throw new RuntimeException("failed to get location.", e);
            }
        }
    }

    public ContactPerson(String name, String email, String lastname, String company,String location, String coursesList, String dateSent) {
        setName(name);
        setEmail(email);
        setLastname(lastname);
        setCompany(company);
        setLocation(location);
        setCourseList(coursesList);
        setSentDate(dateSent);
    }

    public static List<ContactPerson> whereHasLocation() {
        return where(LOCATION + "<>''");
    }

    public static boolean createContact(String city, String country, String email) throws GeoServiceException {
        ContactPerson contact = getContactPerson(city, country, email);

        return contact.saveIt();
    }

    private static ContactPerson getContactPerson(String city, String country, String email) throws GeoServiceException {
        LocationProviderService locationProviderService = new LocationProviderService();
        String location = country + "/" + city;
        Location storedLocation = locationProviderService.getLocationForName(location);
        if (storedLocation == null) {
            locationProviderService.addLat_LongToMemory(country, city);
        }

        ContactPerson contact = new ContactPerson("todo name", email, "todo last name", "todo company", location);

        return contact;
    }

    public static boolean createContacts(List<ContactPerson> newContacts) {
        boolean returnValue;

        if(newContacts.isEmpty()) {
            returnValue = false;
        } else {
            for(int i = 0; i < newContacts.size(); i++) {
                ContactPerson contact = newContacts.get(i);
                contact.saveIt();
            }
            returnValue = true;
        }
        return returnValue;
    }

    public String getName() {
        return getAttribute(FIRSTNAME);
    }

    public void setName(String name) {
        setAttribute(FIRSTNAME, name);
    }

    public String getEmail() {
        return getAttribute(EMAIL);
    }

    public void setEmail(String email) {
        setAttribute(EMAIL, email);
    }

    public String getLastname() {
        return getAttribute(LASTNAME);
    }

    public void setLastname(String lastname) {
        setAttribute(LASTNAME, lastname);
    }

    public String getCompany() {
        return getAttribute(COMPANY);
    }

    public void setCompany(String company) {
        setAttribute(COMPANY, company);
    }

    public String getCoursesSent() {
        return getAttribute(COURSES_SENT);
    }

    public void setCoursesSent(String coursesSent) {
        setAttribute(COURSES_SENT, coursesSent);
    }

    public String getDateSent() {
        return getAttribute(DATE_SENT);
    }

    private void setAttribute(String name, String value) {
        attributes.put(name, value);
        set(name.toLowerCase(), value);
    }

    public double getDoubleAttribute(String name) {
        return (double) get(name);
    }

    public String getAttribute(String name) {
        Object o = get(name);
        if(o != null)
            return o.toString();
        return "";
    }

    public Set<String> getAttributeKeys() {
        return getMetaModel().getAttributeNamesSkipId();
    }

    public static List<ContactPerson> getContactListFromCompany(String company) {
        return where("company = ?", company);
    }

    public static ContactPerson getContactByEmail(String emailAddress) {
        LazyList<ContactPerson> list = where("email = ?", emailAddress);
        if (list.size()> 0)
            return list.get(0);
        return null;
    }

    public void setLocation(String location) {
        setAttribute(LOCATION, location);
    }

    public String getLocation() {
        return getAttribute(LOCATION);
    }

    public Location getGeoCoordinates() {
        return new Location(getLocation(), getDoubleAttribute(LATITUDE), getDoubleAttribute(LONGITUDE));
    }

    public static ContactPerson getContactById(Integer contactId) {
        LazyList<ContactPerson> list = where("id = ?", contactId.intValue());
        if (list.size()> 0)
            return list.get(0);
        return null;
	}
    public boolean AddToCourse(String courseId) {
        int participantId = (int) getId();

        Participant contactParticipant = new Participant(new Integer(participantId), new Integer(courseId));

        return contactParticipant.save();
    }

    public String errorMessage() {
        return "Unable to register participants";
    }

    public void setCourseList(String coursesList) {
        set("courses_sent", coursesList);
    }

    public  void setSentDate(String sentDate) {
        set("date_sent", sentDate);
    }

    public Object getCoursesList() {
        return get("courses_sent");
    }

    public Object getSentDate() {
        return get("date_sent");
    }

    public void setConsentRequestDate(LocalDate requestDate) {
        set(CONSENT_REQUEST_DATE, DateUtil.asDate(requestDate));
    }

    public void setConsentSend(LocalDate requestDate) {
        set(CONSENT_SENT, DateUtil.asDate(requestDate));
    }

    public void setConsentReceived(LocalDate receivedDate) {
        set(CONSENT_RECEIVED, DateUtil.asDate(receivedDate));
    }

    public LocalDate getConsentRequestDate() {
        return DateUtil.asLocalDate((Date) get(CONSENT_REQUEST_DATE));
    }

    public LocalDate getConsentSend() {
        final Date date = (Date) get(CONSENT_SENT);
        return date != null ? DateUtil.asLocalDate(date) : null;
    }

    public LocalDate getConsentReceivedDate() {
        return DateUtil.asLocalDate((Date) get(CONSENT_RECEIVED_DATE));
    }

    public LocalDate getConsentReceived() {
        final Date date = (Date) get(CONSENT_RECEIVED);
        return date != null ? DateUtil.asLocalDate(date) : null;
    }

    public static LazyList<ContactPerson> getContactsWithoutConsentRequest() {
        return where("consent_request_date is null");
    }
}
