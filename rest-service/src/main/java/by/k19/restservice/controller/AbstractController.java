package by.k19.restservice.controller;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

    protected String getRemoteAddress(HttpServletRequest request) {
        return request.getRemoteHost() + ":" + request.getRemotePort();
    }
}
