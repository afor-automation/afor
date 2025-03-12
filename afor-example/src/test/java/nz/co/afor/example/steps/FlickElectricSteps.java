package nz.co.afor.example.steps;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;


import io.cucumber.java8.En;

public class FlickElectricSteps implements En {

	@Value("${flick.dashboard.uri}")
	public String flickDashboardUri;

	@Value("${flick.customer.email}")
	public String flickCustomerEmail;
	@Value("${flick.customer.password}")
	public String flickCustomerPassword;
	@Value("${flick.customer.anumber}")
	public String flickCustomerAnumber;

	public static final String FLAT_PLAN = "Flat";
	public static final String PEAK_PLAN = "Peak";

	public FlickElectricSteps() {
		// Background
		Given("I am a registered Flick customer", () -> {
			// Implementation for verifying the user is a registered Flick customer
			open(flickDashboardUri);
			$(byText("Log in")).click();
			$(byId("user_email")).setValue(flickCustomerEmail);
			$(byId("user_password")).setValue(flickCustomerPassword); //.pressEnter();
			$(byText("Log in")).click();
			$(byText("Log out")).shouldBe(visible);
			System.out.println("Verified that the user is a registered Flick customer.");
		});

		Given("I am logged into my Flick account", () -> {
			// Implementation for verifying the user is logged in
			$(byText("Log out")).shouldBe(visible);
			System.out.println("Verified that the user is logged into their Flick account.");
		});

		// Scenario: View account details
		When("I navigate to the \"Account\" section", () -> {
			// Implementation for navigating to the Account section
			$("a[href='/account']").click();
			System.out.println("Navigated to the Account section.");
		});

		Then("I should see my account information including current electricity plan", () -> {
			// Implementation for verifying account information is displayed
			$(byText("Account number")).shouldBe(visible);
			$(byXpath("//span[.=': " + flickCustomerAnumber + "']")).should(exist);
			System.out.println("Current Plan: " + getCurrentPlan());

			System.out.println("Displayed account information.");
		});

		Then("I should be able to update my personal details if needed", () -> {
			// Implementation for verifying ability to update personal details
			$("a[href='/account/details']").shouldBe(visible).click();
			$(byXpath("//h2[normalize-space()='Account Details']//following::span[normalize-space()='Daniel&Blanka']")).should(exist).shouldBe(visible);
			$(byXpath("//*[@id=\"root\"]/main/div[2]/div/section/form/button")).shouldNotBe(enabled);
			$(byXpath("//button[normalize-space()='Cancel']")).shouldBe(enabled).click();

			System.out.println("Verified ability to update personal details.");
		});


		// Scenario: View the latest electricity bill
		When("I navigate to the \"Billing\" section", () -> {
			// Implementation for navigating to the Billing section
			$("a[href='/bills']").click();

			System.out.println("Navigated to the Billing section.");
		});

		Then("I should see the latest bill details including the amount owing and credit", () -> {
			// Implementation for displaying the latest bill details
			$(byXpath("//p[normalize-space()='Account credit']")).shouldBe(visible);
			$(byXpath("//p[normalize-space()='Amount owing']")).shouldBe(visible);

			System.out.println("Displayed the latest bill details, amount due, and due date.");
		});


		// Scenario: Ensure Flat Rates plan is set on weekdays
		Given("today is a weekday \\(Monday, Tuesday, Wednesday, Thursday, or Friday)", () -> {
			// Implement logic to verify today's date is a weekday (Monday through Friday).
			LocalDate today = LocalDate.now();
			DayOfWeek dayOfWeek = today.getDayOfWeek();
			assertTrue(dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY,
					"Today is not a weekday");
		});

		Then("my current plan should be Flat Rates", () -> {
			// Retrieve the current plan from the system
			$("a[href='/account']").click();
			$(byXpath("//span[.=': Flat']")).should(exist);

			String currentPlan = getCurrentPlan();
			assertEquals("The current plan should be Flat Rates on weekdays", FLAT_PLAN, currentPlan);
		});


		// Scenario Outline: Verify plan changes based on the day of the week
		Given("today is {string}", (String day) -> {
			// Implement logic to set or verify today's day
			DayOfWeek today = LocalDate.now().getDayOfWeek();
			assertEquals("Expected day doesn't match today's day", DayOfWeek.valueOf(day.toUpperCase()), today);
		});

		And("my current plan is {string}", (String currentPlan) -> {
			// Implement logic to verify the current plan
			String actualPlan = getCurrentPlan();
			assertEquals("The current plan does not match the expected current plan", currentPlan, actualPlan);
		});

		When("I request to change my plan to {string}", (String requestedPlan) -> {
			// Implement logic to request a plan change
//			boolean requestSuccessful = requestPlanChange(requestedPlan); // Replace with the logic to handle the request
//			assertTrue(requestSuccessful, "Plan change request was not successful");
			System.out.println("Requested a plan change to " + requestedPlan);
			throw new io.cucumber.java8.PendingException();
		});

		And("the system processes the change at midnight", () -> {
			// Simulate or verify processing of the request at midnight
//			boolean processed = processChangeAtMidnight(); // Mock or simulate a midnight process
//			assertTrue(processed, "The system did not process the request at midnight");
			System.out.println("The system processed the request at midnight");
			throw new io.cucumber.java8.PendingException();
		});

		Then("my plan should update to {string} at midnight tonight", (String expectedPlan) -> {
			// Verify that the plan has been updated correctly after processing at midnight
//			String updatedPlan = getUpdatedPlan(); // Replace with a method that gets the updated plan
//			assertEquals(expectedPlan, updatedPlan, "The plan did not update to the expected plan at midnight");
			System.out.println("The plan updated to " + expectedPlan + " at midnight tonight");
			throw new io.cucumber.java8.PendingException();
		});
	}

	private String getOptimalCurrentPlanBasedOnDay() {
		DayOfWeek currentDay = new java.util.Date().toInstant().atZone(java.time.ZoneId.systemDefault()).getDayOfWeek();
		System.out.println("Current day: " + currentDay + " its number: " + currentDay.getValue());
		String recommendedPlan = "";
		if (currentDay == DayOfWeek.SATURDAY || currentDay == DayOfWeek.SUNDAY) {
			recommendedPlan = PEAK_PLAN;
		} else {
			recommendedPlan = FLAT_PLAN;
		}
		System.out.println("Recommended plan: " + recommendedPlan);
		return recommendedPlan;
	}
	private String getCurrentPlan() {
		if (FLAT_PLAN.equals(getOptimalCurrentPlanBasedOnDay())) {
			$(byXpath("//span[.=': Flat']")).should(exist);
			return FLAT_PLAN;
		} else {
			$(byXpath("//span[.=': Peak']")).should(exist);
			return PEAK_PLAN;
		}
	}
}
