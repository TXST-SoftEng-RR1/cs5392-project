package edu.txstate.library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController {
    @RequestMapping(value={"/"})
    public String indexPage() {
        return "index";
    }
}
