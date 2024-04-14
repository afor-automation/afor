# Welcome to the Afor framework example
To run the Afor framework example, you will need to have JDK 17 or greater, Maven and your preferred IDE installed. Chrome and chromedriver may also be required for some scenarios. \
For more detailed installation, [follow the installation instructions](https://www.afor.co.nz/framework/installation/install-document.html) to get started

## Structuring your code
The intention of the framework is to loosely use an MVC pattern, to manage code.

### Views
Views are generally used for what would sometimes be referred to as a page object model

### Models
Models are used to store data model information relating to your scenarios data, such as users, accounts etc.

### Controllers
Step definitions are used to connect your cucumber features to your java definitions, over time though having large amounts of code in your step definitions becomes difficult to manage.

Move common functionality into controller classes, this allows for reuse of code and prevents code duplication.

## Interacting with a web page
Under the features directory, you will find a feature named `GoogleSearch.feature` \
This is a browser based feature, which searches for a term and validates the results on the page

The feature uses natural language to describe a scenario, which searches for keywords using the search engine
```
Scenario: Search for an example piece of text
  Given I am on the home page
  When I search for "afor automation"
  Then I should see some search results
```

A step definition is created for each of the steps in the search feature
```
When("^I search for \"([^\"]*)\"$", (String searchString) -> googleView.search(searchString));
```

The `GoogleView` class contains methods to return fields on the page, note that the page is annotated with a `@Component` annotation - this allows us to use properties and other parts of the framework in our classes.
```
@Component
public class GoogleView {
    public SelenideElement getSearchField() {
        return $(By.name("q"));
    }
```

The view exposes a method to wrap up performing a web search, this is the method used in the step definitions\
Assertions such as `should(exist)` both prove that the content is loading as expected, however it also enables stability as the automation framework will poll page elements until the condition is met or until a timeout occurs.
```    
public void search(String searchString) {
    getSearchButton().should(exist);
    getSearchField().click();
    getSearchField().should(be(focused));
    getSearchField().setValue(searchString);
    getSearchButton().click();
}
```

## Calling a service
Service calls such as REST services or SOAP services are used in the framework in a similar way to browser based testing.

The `RestApi.feature` contains REST service calls, to show how to run GET and POST requests to endpoints.\
The service calls use data models to serialise responses.

```
Scenario: Get a list of Posts from a REST service
  When I call the GET Posts service
  Then the GET Posts response code should be 200
  And the response should have a lists of Posts
```

The step definition for the service call is as follows, returning a service response\
In this case, the example step definition uses the classic style rather lambda expressions. Either option is suitable
```
@When("^I call the GET Posts service$")
public void iCallTheGETPostsService() {
    getPostsResponse = postsApi.getPosts();
}
```

The `PostsApi` class uses the `Get` and `Post` classes from the framework, in order to call the REST services\
Note that the `api.posts.uri` value is used as the URI to call the service and is defined in the `application.properties` or test environment properties.

The load order of properties is `application.properties` first, then environment specific properties, finally by runtime properties.\
This allows overriding property values for each test environment or at runtime.
```
@Component
public class PostsApi {
    @Autowired
    Get get;

    @Autowired
    Post post;

    @Value("${api.posts.uri}")
    String apiPostsUri;
```

When it comes to calling the API, the get or post request is able to serialise the response back as an object.\
In the example, the data model is used to serialise responses.\
First the headers are set to define the acceptable response types, followed by the get request call to the endpoint with the expected response type.
```
public ResponseEntity<nz.co.afor.model.Post[]> getPosts() {
    get.getHeaders().set(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
    return get.request(apiPostsUri, nz.co.afor.model.Post[].class);
}
```
## Viewing reports
After running the test suite, you can view the reports under target/afor/index.html