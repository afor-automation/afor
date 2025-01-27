import io.cucumber.java8.En;
import nz.co.afor.framework.minify.MinifyHtml;
import nz.co.afor.framework.minify.RegexMinifyHtml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MinifySteps implements En {
    private String html;
    private String minified;

    public MinifySteps() {
        Given("I have the html", (String html) ->
                this.html = html);
        When("I minify the html using minify html", () ->
                this.minified = new MinifyHtml().minify(html));
        Then("the minified html should be {string}", (String expectedResult) ->
                assertThat("the minified html should match the expected result", minified, equalTo(expectedResult)));
        When("I minify the html using regex minify html", () ->
                this.minified = new RegexMinifyHtml().minify(html));
    }
}
