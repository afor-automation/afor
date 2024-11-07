package nz.co.afor.framework.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
    public void iHaveANewList() {
        list = new ArrayList<>();
        list.add("first");
        list.add("second");
        list.add("third");
    }

    @When("^I get the first list value$")
    public void iGetTheFirstListValue() {
        result = getFirst(list);
    }

    @Then("^the first list value should be returned$")
    public void theFirstListValueShouldBeReturned() {
        assertThat(result, equalTo(list.getFirst()));
    }

    @When("^I get the last list value$")
    public void iGetTheLastListValue() {
        result = getLast(list);
    }

    @Then("^the last list value should be returned$")
    public void theLastListValueShouldBeReturned() {
        assertThat(result, equalTo(list.getLast()));
    }

    @When("^I get any list value$")
    public void iGetAnyListValue() {
        result = anyOf(list);
    }

    @Then("^a list value should be returned$")
    public void aListValueShouldBeReturned() {
        assertThat(result, is(not(nullValue())));
    }
}
