package com.example.Login.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class CustomErrorController implements ErrorController {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handle404(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error-404");
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericError(Exception ex) {

        if (((NoResourceFoundException) ex).getBody().getStatus() == 404) {
            ModelAndView modelAndView = new ModelAndView("error-404");
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            modelAndView.addObject("message", ex.getMessage());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            modelAndView.addObject("message", ex.getMessage());
            return modelAndView;
        }
    }
}
