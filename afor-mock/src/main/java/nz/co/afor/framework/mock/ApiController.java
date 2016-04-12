package nz.co.afor.framework.mock;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Matt Belcher on 12/04/2016.
 */
@Controller
public class ApiController {
    @RequestMapping(method = RequestMethod.GET, value = "/api/plainResponse")
    public
    @ResponseBody
    String plainResponse() {
        return "plainResponse";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/plainResponseWithParameter")
    public
    @ResponseBody
    String plainResponseWithParameter(@RequestParam(value = "parameter", required = false, defaultValue = "default parameter") String parameter, Model model) {
        model.addAttribute("parameter", parameter);
        return "plainResponseWithParameter";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/object")
    public
    @ResponseBody
    ApiModel object() {
        return new ApiModel();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/plainResponse")
    public
    @ResponseBody
    String plainResponsePost() {
        return "plainResponse";
    }
}
