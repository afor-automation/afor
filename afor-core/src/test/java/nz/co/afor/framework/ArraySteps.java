package nz.co.afor.framework;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static nz.co.afor.framework.Collections.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


/**
 * Created by Matt on 6/03/2018.
 */
public class ArraySteps {
    private String[] array;
    private String result;

    @Given("^I have a new array$")
    public void iHaveANewArray() throws Throwable {
        array = new String[]{"first", "second", "third"};
    }

    @When("^I get the first array value$")
    public void iGetTheFirstArrayValue() throws Throwable {
        result = getFirst(array);
    }

    @Then("^the first array value should be returned$")
    public void theFirstArrayValueShouldBeReturned() throws Throwable {
        assertThat(result, equalTo(array[0]));

    }

    @When("^I get the last array value$")
    public void iGetTheLastArrayValue() throws Throwable {
        result = getLast(array);
    }

    @Then("^the last array value should be returned$")
    public void theLastArrayValueShouldBeReturned() throws Throwable {
        assertThat(result, equalTo(array[array.length - 1]));
    }

    @When("^I get any array value$")
    public void iGetAnyArrayValue() throws Throwable {
        result = anyOf(array);
    }

    @Then("^an array value should be returned$")
    public void aNArrayValueShouldBeReturned() throws Throwable {
        assertThat(result, is(not(nullValue())));
    }
}
