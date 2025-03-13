package nz.co.afor.example.steps;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;

import com.codeborne.selenide.Selenide;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TradmeSteps {

	@Value("${tradme.uri}")
	public String tradme;

	String searchText = "laptop";

	@When("I open Tradme website")
	public void iOpenTradmeWebsite() {
		open(tradme);
	}

	@Then("I should see a search field")
	public void iShouldSeeASearchField() {
		$(byId("search")).shouldBe(visible);
	}

	@When("I type a product name")
	public void iTypeAProductName() {
		$(byId("search")).setValue(searchText).pressEnter();
	}

	@Then("I should see a list of products with filtering criteria")
	public void iShouldSeeAListOfProductsWithFilteringCriteria() {
		$(byXpath("//h3[@class='tm-search-header-result-count__heading ng-star-inserted']")).shouldHave(text("Showing")).shouldHave(text("results for '" + searchText + "'"));
		$(byXpath("//button[contains(text(),'Price')]")).shouldBe(visible).click();

		// Setting the price range
		$(byXpath("//button[contains(text(),' Clear ')]")).shouldNotBe(enabled);
		String resultTextFull = $(byXpath("//button[contains(text(),'View')]")).shouldBe(visible).shouldBe(enabled).getText().replace("View ", "");;

		$("select[name='min_option']").shouldBe(visible).selectOption(10);
		Selenide.sleep((long) (Math.random() * 3000 + 1000)); // Add delay as not sure how to check for the spinner symbol on the button
		String resultTextFrom = $(byXpath("//button[contains(text(),'View')]")).shouldBe(visible).shouldBe(enabled).getText().replace("View ", "");;

		$(byXpath("//button[contains(text(),' Clear ')]")).shouldBe(enabled);
		$("select[name='max_option']").shouldBe(visible).selectOption("$1000");
		Selenide.sleep((long) (Math.random() * 3000 + 1000)); // Add delay as not sure how to check for the spinner symbol on the button
		String resultTextTo = $(byXpath("//button[contains(text(),'View')]")).shouldBe(visible).shouldBe(enabled).getText().replace("View ", "");;

		// filtering results by price range
		$(byXpath("//button[contains(text(),'View')]")).shouldBe(visible).shouldBe(enabled).click();
		Selenide.sleep((long) (Math.random() * 3000 + 1000)); // Add delay

		System.out.println("All results: " + resultTextFull + " from " + resultTextFrom + " to " + resultTextTo);

		assertTrue((getResultCount(resultTextFull) >= getResultCount(resultTextFrom) &&
				            getResultCount(resultTextFrom)>= getResultCount(resultTextTo)));
		Selenide.sleep((long) (Math.random() * 9000 + 1000)); // Add delay
		$(byXpath("//button[contains(text(),'Price: $50 - $1000')]")).shouldBe(visible);

		// Verifies how many results are shown after we filter our search by a price range
		$(byXpath("//h3[contains(@class, 'tm-search-header-result-count__heading')][contains(text(), \"Showing " + resultTextTo + " for '" + searchText + "'\")]")).should(exist);
	}

	private int getResultCount(String resultText) {
		// Define a regex pattern to capture the number
		Pattern pattern = Pattern.compile("\\d+(,\\d+)*"); // Matches numbers with commas (e.g., 8,336)
		Matcher matcher = pattern.matcher(resultText);

		int resultCount = 0;
		if (matcher.find()) {
			// Remove the commas and parse the matched number as an integer
			resultCount = Integer.parseInt(matcher.group().replace(",", ""));
		}
		return resultCount;
	}
}
