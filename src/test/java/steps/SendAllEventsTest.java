package steps;

import com.odde.massivemailer.model.MailLog;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.javalite.activejdbc.Base;
import steps.driver.WebDriverFactory;
import steps.driver.WebDriverWrapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class SendAllEventsTest {
    private static final String BASE_URL = "http://localhost:8070/massive_mailer/coursedlist.jsp";
    private static final String ADD_CONTACT_URL = "http://localhost:8070/massive_mailer/add_contact.jsp";
    private static final String ADD_EVENT_URL = "http://localhost:8070/massive_mailer/add_event.jsp";

    private WebDriverWrapper driver = WebDriverFactory.getDefaultDriver();
    private int numberOfEvents, numberOfEventsInLocation;
    private int numberOfContacts, numberOfContactsInLocation;

    public void addContact(String email,String location) {
        driver.visit(ADD_CONTACT_URL);
        driver.setTextField("email", email);
        driver.setDropdownValue("location", location);
        driver.clickButton("add_button");
    }

    public void cleanReportTable() {
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:cucumber_test.db", "", "");
        Base.openTransaction();
        MailLog.deleteAll();
        Base.close();
    }

    private void addCourses(int offsetDate, String location) {
        LocalDateTime d = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        d.plusMonths(offsetDate);

        driver.visit(ADD_EVENT_URL);
        driver.setTextField("coursename", "A course");
        driver.setTextField("duration", "30");
        String[] locations = location.split("/");
        assert locations.length == 2;
        driver.setDropdownValue("country", locations[0]);
        driver.setTextField("city", locations[1]);
        driver.setTextField("startdate", d.format(f));
        driver.setTextField("address", "odd-e");
        driver.setTextField("coursedetails", "csd");
        driver.setTextField("instructor", "terry");

        driver.clickButton("save_button");
        driver.waitforElementByTagName("body");
        System.out.print("creating at: ");
        System.out.println(location);
        System.out.flush();
    }

    @Given("^visit event list page$")
    public void VisitEventListPage() throws Throwable {
        cleanReportTable();
        this.numberOfEvents = 0;
        this.numberOfContacts = 0;
        driver.visit(BASE_URL);
    }


    @When("^(\\d+) out of (\\d+) contacts are in Singapore$")
    public void numberOfContactIs(int numberOfContactsInLocation, int numOfContacts) throws Throwable {
        this.numberOfContactsInLocation = numberOfContactsInLocation;
        this.numberOfContacts = numOfContacts;
        MyStepdefs contactTests = new MyStepdefs();
        for (int i = 0; i < this.numberOfContacts; i++) {
            contactTests.addAContact("test@test" + i + ".com", "Singapore", "Singapore");
        }
    }

    @When("^We have below number of contacts at each location:$")
    public void createContactsForLocations(DataTable dtContactsPerLocation) throws Throwable {
        List<List<String>> contacts = dtContactsPerLocation.raw();
        contacts = contacts.subList(1, contacts.size());//skip header row
        int totalNumberOfContacts = 0;
        for (List<String> location : contacts) {
            MyStepdefs contactTests = new MyStepdefs();
            this.numberOfContactsInLocation = Integer.parseInt(location.get(2));
            for (int i = 0; i < this.numberOfContactsInLocation; i++) {
                totalNumberOfContacts++;
                contactTests.addAContact("test@test" + totalNumberOfContacts + ".com", location.get(0) + "/"+location.get(1));
            }
        }
        this.numberOfContacts = totalNumberOfContacts;
    }

    @When("^(\\d+) out of (\\d+) events are in Singapore$")
    public void numberOfEventIs(int numberOfEventsInLocation, int numberOfEvents) throws Throwable {
        this.numberOfEventsInLocation = numberOfEventsInLocation;
        this.numberOfEvents = numberOfEvents;
        EventTests eventTests = new EventTests();
        for (int i = 0; i < this.numberOfEventsInLocation; i++) {
            eventTests.addCourseWithCountryAndCity("Event " + i, "Singapore", "Singapore", "2017-05-17");
        }
        for (int i = 0; i < this.numberOfEvents - this.numberOfEventsInLocation; i++) {
            eventTests.addCourseWithCountryAndCity("Event " + i, "Not-Singapore", "Not-Singapore", "2017-05-17");
        }
    }

    @When("^We have below number of events at each location:$")
    public void createEventsForLocations(DataTable dtEventsPerLocation) throws Throwable {
        List<List<String>> events = dtEventsPerLocation.raw();
        events = events.subList(1, events.size());//skip header row
        int totalNumberOfEvent = 0;
        for (List<String> oneLocation : events) {
            EventTests eventTests = new EventTests();
            this.numberOfEventsInLocation = Integer.parseInt(oneLocation.get(2));
            for (int i = 0; i < this.numberOfEventsInLocation; i++) {
                totalNumberOfEvent++;
                eventTests.addCourseWithCountryAndCity("Event " + totalNumberOfEvent, oneLocation.get(0), oneLocation.get(1), "2017-05-17");
            }
        }
        this.numberOfEvents = totalNumberOfEvent;
    }


    @When("^I click send button$")
    public void ClickSendButton() throws Throwable {
        driver.visit(BASE_URL);
        driver.clickButton("send_button");
    }

    @Then("^([^\"]*) contact\\(s\\) receive an email that contains ([^\"]*)$")
    public void contactReceiveEmailContainsEvents(String numberOfEmailRecipients, String numberOfEventsInEmail) throws Throwable {
        String expectedMessage = String.format("%s emails contain %s events sent.", numberOfEmailRecipients, numberOfEventsInEmail);
        driver.expectElementWithIdToContainText("message", expectedMessage);
    }

    @Then("It should send out emails:")
    public void shouldSendOutEmails(DataTable dtEmails) {
        List<List<String>> emails = dtEmails.raw();
        emails = emails.subList(1, emails.size());//skip header row
        for (List<String> oneLocation : emails) {
            String expectedMessage = String.format("%s emails contain %s events sent.", oneLocation.get(1), oneLocation.get(2));
            driver.expectElementWithIdToContainText("message", expectedMessage);
        }
    }

    @Then("It should not send out emails")
    public void shouldNotSendOutEmails() {
        String expectedMessage = String.format("0 emails contain 0 events sent.");
        driver.expectElementWithIdToContainText("message", expectedMessage);
    }

    @Given("^There is a contact \"([^\"]*)\" at \"([^\"]*)\"$")
    public void there_is_a_contact_at(String arg1, String arg2) throws Throwable {
        Stream<String> list = Stream.of(arg2.split(","));
        list.map(i -> i.trim()).forEach(i -> addContact(arg1, i));
    }

    @Given("^There is a contact \"([^\"]*)\" at Japan/(.*?)$")
    public void there_is_a_contact_at_Japan(String email, String city_in_japan) throws Throwable {
        MyStepdefs contactTests = new MyStepdefs();
        contactTests.addAContact(email, "Japan", city_in_japan);
    }

    @Given("^there are \"([^\"]*)\" courses at each of \"([^\"]*)\"$")
    public void there_are_courses_at(String count_string, String locations) throws Throwable {
        int number = Integer.parseInt(count_string);
        for (int i = 0; i < number; i++) {
            Stream.of(locations.split(",")).forEach(s -> addCourses(30, s));
        }
    }

    @Given("^add contact \"([^\"]*)\" at Japan/Tokyo$")
    public void add_contact_at_Japan_Tokyo(String email) throws Throwable {
        there_is_a_contact_at_Japan(email, "Tokyo");
    }
}
