package nz.co.afor.framework.minify;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minifies html strings using regular expressions
 */
public class RegexMinifyHtml implements Minify {

    private final Map<String, String> expressions = new LinkedHashMap<>();

    public RegexMinifyHtml() {
        withExpression("[\n\t]", "")
                .withExpression("> +<", "><")
                .withExpression("<head>.*?</head>", "")
                .withExpression("<style>.*?</style>", "")
                .withExpression("<script>.*?</script>", "")
                .withExpression("<meta>.*?</meta>", "")
                .withExpression("<meta[^>]+/>", "")
                .withExpression("<link>.*?</link>", "")
                .withExpression("<link[^>]+/>", "")
                .withExpression("<svg>.*?</svg>", "")
                .withExpression("<[a-z]+ */>", "");
    }

    public RegexMinifyHtml withExpression(String expression, String replacement) {
        expressions.put(expression, replacement);
        return this;
    }

    public Map<String, String> getExpressions() {
        return expressions;
    }

    @Override
    public String minify(String input) {
        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            input = input.replaceAll(entry.getKey(), entry.getValue());
        }
        return input;
    }
}
