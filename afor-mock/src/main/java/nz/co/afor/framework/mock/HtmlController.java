package nz.co.afor.framework.mock;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Matt Belcher on 18/08/2015.
 */
@Controller
public class HtmlController {

    @RequestMapping(method = RequestMethod.GET, value = "/plainText")
    public String plainText() {
        return "plainText";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/plainTextWithParameter")
    public String plainTextWithParameter(@RequestParam(value = "parameter", required = false, defaultValue = "default parameter") String parameter, Model model) {
        model.addAttribute("parameter", parameter);
        return "plainTextWithParameter";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/plainText")
    public String plainTextPost() {
        return "plainText";
    }
}
