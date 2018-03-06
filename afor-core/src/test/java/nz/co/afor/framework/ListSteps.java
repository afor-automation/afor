package nz.co.afor.framework;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static nz.co.afor.framework.Collections.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by Matt on 6/03/2018.
 */
public class ListSteps {
    private List<String> list;
    private String result;

    @Given("^I have a new list$")
    public void iHaveANewList() throws Throwable {
        list = new ArrayList<>();
        list.add("first");
        list.add("second");
        list.add("third");
    }

    @When("^I get the first list value$")
    public void iGetTheFirstListValue() throws Throwable {
        result = getFirst(list);
    }

    @Then("^the first list value should be returned$")
    public void theFirstListValueShouldBeReturned() throws Throwable {
        assertThat(result, equalTo(list.get(0)));
    }

    @When("^I get the last list value$")
    public void iGetTheLastListValue() throws Throwable {
        result = getLast(list);
    }

    @Then("^the last list value should be returned$")
    public void theLastListValueShouldBeReturned() throws Throwable {
        assertThat(result, equalTo(list.get(list.size() - 1)));
    }

    @When("^I get any list value$")
    public void iGetAnyListValue() throws Throwable {
        result = anyOf(list);
    }

    @Then("^a list value should be returned$")
    public void aListValueShouldBeReturned() throws Throwable {
        assertThat(result, is(not(nullValue())));
    }
}
