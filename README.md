# Afor Automation Test Framework

The Afor framework is designed to allow creating automated test suites, which can target various applications and
services such as web browser based applications, REST and SOAP services, native and hybrid mobile applications.\
Other applications can be added into the framework as desired, such as windows application capabilities.

## Reports

Make use of consistent reports across your automation, regardless of the system you are testing against. \
The reports should be available in your projects `target/afor` directory. \
The reports show high level information at the top of the report, with in-depth details further down.
![example report](./design/example-report.png)

## Example test suite

To help get you started, a test suite which shows examples of both web browser based features and REST service features
can be found under [afor-example](https://github.com/afor-automation/afor/tree/master/afor-example)

## Getting started / Installation

The framework requires jdk 21 or later, maven, an IDE of your choice (unless running headless), along with a browser and
associated drivers if performing web browser testing or simulators/real devices and appium if testing mobile
applications.

### Basic installation for Web and Service automation

* Download and install JDK 21 or later \
  if on Linux or MacOS then it is recommended to use a package manager to install
* Install Chrome or a browser of your choice, if behind a proxy then you may need to download the relevant drivers
  manually and put them into your path \
  [Chrome and chromedriver](https://googlechromelabs.github.io/chrome-for-testing/) are often used, however other
  browsers are also supported.
* Install an IDE of your choice, such as [Intellij Idea](https://www.jetbrains.com/idea/)
  or [Eclipse](https://www.eclipse.org/topics/ide/) \
  If your are running headless, such as in a pipeline then this step is not required

### Additional installation steps for Mobile automation

* Download and install [npm](https://www.npmjs.com/package/npm)
* [Install appium](https://appium.io/docs/en/latest/quickstart/install/) `npm install -g appium`
* Install [Android Studio](https://developer.android.com/studio/install)
  and [set up a new virtual device](https://developer.android.com/studio/run/managing-avds) under AVD Manager
* If running on MacOS, download and install [XCode](https://developer.apple.com/xcode/) and set up
  a [new simulator](https://developer.apple.com/documentation/safari-developer-tools/installing-xcode-and-simulators)
* If you intend on automating iOS, either run them from a Mac or using a cloud based device from a provider such as
  Sauce labs or Test object

## Running the automation

To run the automation features from your IDE, create a new JUnit run configuration against the RunTest unit test.\
The parameters for the test suite allow different scenarios to be run.

| Description                                                                                               | Example                                                                                                           |
|-----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| Run all scenarios which have test tags In dev environment, using chrome as the browser                    | -Dspring.profiles.active=dev -Dcucumber.filter.tags="@test" -Dbrowser=chrome                                      |
| Run all scenarios which don’t have wip or opendefect tags In dev environment, using chrome as the browser | -Dspring.profiles.active=dev -Dcucumber.filter.tags="not @wip and not @opendefect" -Dbrowser=chrome               |
| Run the scenario on line 7 of GoogleSearch.feature In dev environment, using chrome as the browser        | -Dspring.profiles.active=dev -Dcucumber.features="classpath:features/web/GoogleSearch.feature:7" -Dbrowser=chrome |
| Running a scenario, using maven from the command line or from a build server                              | mvn test -Dspring.profiles.active=dev -Dcucumber.filter.tags="not @wip and not @opendefect" -Dbrowser=chrome      |

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

The `GoogleView` class contains methods to return fields on the page, note that the page is annotated with a
`@Component` annotation - this allows us to use properties and other parts of the framework in our classes.

```
@Component
public class GoogleView {
    public SelenideElement getSearchField() {
        return $(By.name("q"));
    }
```

The view exposes a method to wrap up performing a web search, this is the method used in the step definitions\
Assertions such as `should(exist)` both prove that the content is loading as expected, however it also enables stability
as the automation framework will poll page elements until the condition is met or until a timeout occurs.

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

Service calls such as REST services or SOAP services are used in the framework in a similar way to browser based
testing.

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
Note that the `api.posts.uri` value is used as the URI to call the service and is defined in the
`application.properties` or test environment properties.

The load order of properties is `application.properties` first, then environment specific properties, finally by runtime
properties.\
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
First the headers are set to define the acceptable response types, followed by the get request call to the endpoint with
the expected response type.

```
public ResponseEntity<nz.co.afor.model.Post[]> getPosts() {
    get.getHeaders().set(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
    return get.request(apiPostsUri, nz.co.afor.model.Post[].class);
}
```

## Viewing reports

After running the test suite, you can view the reports under `target/afor/index.html`

## Other configuration properties

There are many other options available to configure through your configuration properties or at runtime\
A list of possible configuration options are available below

### afor-api

#### API host and ssl config

`api.hostUrl` \
`api.ssl.selfsigned`

#### Proxy configuration

`proxy.username` \
`proxy.password` \
`proxy.domain` \
`proxy.address`

#### Connection pooling

`api.ssl.selfsigned` \
`api.pool.connections.max` \
`api.pool.connections.route.max` \
`api.pool.inactivity.validate` \
`api.pool.connections.timeToLive` \
`api.pool.connect.timeout` \
`api.pool.socket.timeout`

### afor-core

#### Encryption configuration

`nz.co.afor.encrypt.key` \
`nz.co.afor.encrypt.algorithm` \
`nz.co.afor.encrypt.key.algorithm` \
`nz.co.afor.encrypt.iv`

#### Fixtures

`nz.co.afor.fixture.dateformat` \
`nz.co.afor.fixture.timezone` \
`nz.co.afor.fixture.path` \
`nz.co.afor.fixture.dateformat` \
`nz.co.afor.fixture.timezone`

### afor-cucumber

`nz.co.afor.report.heading` \
`nz.co.afor.report.title` \
`nz.co.afor.report.date.format` \
`nz.co.afor.report.date.timezone`

### afor-mobile

`appium.remote.url` \
`appium.platformName` \
`appium.platformVersion` \
`appium.deviceName` \
`appium.udid` \
`appium.app` \
`appium.appPackage` \
`appium.browser` \
`appium.browserName` \
`appium.newCommandTimeout` \
`appium.orientation` \
`appium.autoWebview` \
`appium.noReset` \
`appium.fullReset` \
`appium.autoGrantPermissions` \
`appium.automationName` \
`appium.nativeScreenshot` \
`appium.recreateChromeDriverSessions` \
`appium.appiumVersion` \
`appium.deviceOrientation` \
`appium.ignoreUnimportantViews` \
`appium.phoneOnly` \
`appium.testobjectApiKey` \
`appium.testobjectSessionCreateTimeout` \
`appium.idleTimeout` \
`appium.maxDuration` \
`appium.uiautomator2ServerLaunchTimeout` \
`appium.remote` \
`appium.bundleId`

### afor-web

`browser` \
`selenide.baseUrl` \
`selenide.timeout` \
`selenide.pollingInterval`