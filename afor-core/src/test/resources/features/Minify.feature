Feature: Minify html input

  As an automation engineer
  I want to minify html into smaller payloads
  So that there is less overhead when sending over a network

  Scenario: Minify using the minify html minifier
    Given I have the html
    """
    <html>
        <head>
        </head>
        <br />
        <body>
          <div> <h1 class="title"> Test</h1></div>
        </body>
    </html>
    """
    When I minify the html using minify html
    Then the minified html should be "</head><br><body><div><h1 class=title>Test</h1></div>"

  Scenario: Minify using the regex html minifier
    Given I have the html
    """
    <html>
        <head>
        </head>
        <meta link="test" />
        <br />
        <body>
          <div> <h1 class="title"> Test</h1></div>
        </body>
    </html>
    """
    When I minify the html using regex minify html
    Then the minified html should be "<html><body><div><h1 class=\"title\"> Test</h1></div></body></html>"
