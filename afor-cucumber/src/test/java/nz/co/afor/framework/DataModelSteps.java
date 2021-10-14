package nz.co.afor.framework;

import io.cucumber.java8.En;
import nz.co.afor.framework.model.Customer;
import nz.co.afor.framework.model.Customers;
import org.hamcrest.MatcherAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;

public class DataModelSteps implements En {

    @Autowired
    Customers customers;

    Customer newCustomer;
    Customer getCustomer;

    public DataModelSteps() {
        When("^I add a new item to the customers data model$", this::addCustomer);
        Then("^the customers data model should have (\\d+) (?:entry|entries)$", (Integer entries) ->
                MatcherAssert.assertThat(customers.size(), equalTo(entries)));
        When("^I get from the customers data model$", () ->
                getCustomer = customers.get());
        Then("^the data should match the item added$", () ->
                MatcherAssert.assertThat(getCustomer.getUsername(), equalTo(newCustomer.getUsername())));
        When("^I add a (\\d+) new items to the customers data model$", (Integer numberOfItems) -> {
            for(int item = 0; item < numberOfItems; item++)
                addCustomer();
        });
    }

    private void addCustomer() {
        newCustomer = new Customer();
        newCustomer.setUsername(String.valueOf(new Random().nextInt(5)));
        customers.add(newCustomer);
    }
}
