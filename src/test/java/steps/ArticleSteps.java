package steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import steps.driver.WebDriverWrapper;
import steps.site.MassiveMailerSite;

import java.util.Map;


public class ArticleSteps {

    private MassiveMailerSite site = new MassiveMailerSite();
    private WebDriverWrapper driver = site.getDriver();
    private String courseDetailUrl = site.baseUrl() + "course_detail.jsp";

    @Given("^記事登録ページに遷移$")
    public void 記事登録ページに遷移() throws Throwable {
        site.visit("article_new.jsp");
    }

    @When("^記事情報を入力してサブミット$")
    public void 記事情報を入力してサブミット(DataTable params) throws Throwable {
        Map<String, Object> article = params.asMap(String.class, Object.class);
        driver.setTextField("title", (String) article.get("title"));
        driver.clickButton("submit");
    }

    @Then("^記事一覧ページに\"([^\"]*)\"が表示されている$")
    public void 記事一覧ページに_が表示されている(String string) throws Throwable {
        driver.pageShouldContain(string);
    }


}
