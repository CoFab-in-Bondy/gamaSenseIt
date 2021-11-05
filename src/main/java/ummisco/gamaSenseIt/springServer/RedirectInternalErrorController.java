package ummisco.gamaSenseIt.springServer;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectInternalErrorController implements ErrorController {

    @RequestMapping("/error")
    public String index() {
        return "forward:/index.html";
    }
}
