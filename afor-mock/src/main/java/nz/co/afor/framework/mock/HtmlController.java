package nz.co.afor.framework.mock;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Matt Belcher on 18/08/2015.
 */
@Controller
public class HtmlController {

    @RequestMapping(method = RequestMethod.GET, value = "/plainHtml")
    public String plainText() {
        return "plainText";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plainHtmlWithParameter")
    public String plainTextWithParameter(@RequestParam(value = "parameter", required = false, defaultValue = "default parameter") String parameter, Model model) {
        model.addAttribute("parameter", parameter);
        return "plainTextWithParameter";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/plainHtml")
    public String plainTextPost() {
        return "plainText";
    }
}
