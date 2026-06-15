package com.alert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(value = {
        "/",
        "/sites/{id:\\d+}",
        "/sites/{id:\\d+}/pages",
        "/sites/{id:\\d+}/no-h1",
        "/sites/{id:\\d+}/redirects",
        "/sites/{id:\\d+}/errors",
        "/sites/{id:\\d+}/changes"
    })
    public String spa() {
        return "forward:/index.html";
    }
}
