package nz.co.afor.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.co.afor.api.PostsApi;
import nz.co.afor.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

/**
 * Created by Matt on 12/04/2016.
 */
public class ApiSteps {
    @Autowired
    PostsApi postsApi;

    ResponseEntity<Post[]> getPostsResponse;
    ResponseEntity<Post> getPostResponse;
    ResponseEntity<Post> createPostResponse;

    Post post;

    @When("^I call the GET Posts service$")
    public void iCallTheGETPostsService() {
        getPostsResponse = postsApi.getPosts();
    }

    @Then("^the GET Posts response code should be (\\d+)$")
    public void theGETPostsResponseCodeShouldBe(int responseCode) {
        assertThat(String.format("The GET Posts http response code should be '%s'", responseCode), getPostsResponse.getStatusCode(), is(HttpStatus.valueOf(responseCode)));
    }

    @And("^the response should have a lists of Posts$")
    public void theResponseShouldHaveAListsOfPosts() {
        assertThat("We should have at least 1 post in the response", Objects.requireNonNull(getPostsResponse.getBody()).length, is(greaterThan(0)));
    }

    @When("^I call the GET Posts service with the post id (\\d+)$")
    public void iCallTheGETPostsServiceWithThePostId(int id) {
        getPostResponse = postsApi.getPost(id);
    }

    @Then("^the GET Post response code should be (\\d+)$")
    public void theGETPostResponseCodeShouldBe(int responseCode) {
        assertThat(String.format("The GET Post http response code should be '%s'", responseCode), getPostResponse.getStatusCode(), is(HttpStatus.valueOf(responseCode)));
    }

    @And("^the response should have the id (\\d+)$")
    public void theResponseShouldHaveTheId(int id) {
        assertThat(String.format("The post response should have the id '%s'", id), Objects.requireNonNull(getPostResponse.getBody()).getId(), is(id));
    }

    @Given("^I have a new Post$")
    public void iHaveANewPost() {
        post = new Post();
        post.setTitle("New Post");
    }

    @Given("^I have a new Post with title \"([^\"]*)\"$")
    public void iHaveANewPostWithTitle(String title) {
        post = new Post();
        post.setTitle(title);
    }

    @When("^I call the Create Post service with my Post$")
    public void iCallTheCreatePostServiceWithMyPost() {
        createPostResponse = postsApi.createPost(post);
    }

    @Then("^the Create Post response code should be (\\d+)$")
    public void theCreatePostResponseCodeShouldBe(int responseCode) {
        assertThat(String.format("The Create Post http response code should be '%s'", responseCode), createPostResponse.getStatusCode(), is(HttpStatus.valueOf(responseCode)));
    }
}
