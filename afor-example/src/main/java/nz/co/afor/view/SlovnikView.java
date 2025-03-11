package nz.co.afor.view;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.codeborne.selenide.SelenideElement;

@Component
//It may not be necessary and can be deleted
public class SlovnikView {

	@Value("${slovnik.seznam.uri}")
	public String slovnikSeznamUri;

	public SelenideElement getTranslationField() {
		return $(byName("search"));
	}
}
