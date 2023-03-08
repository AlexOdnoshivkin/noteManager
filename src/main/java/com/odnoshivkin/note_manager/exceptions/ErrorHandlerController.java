package com.odnoshivkin.note_manager.exceptions;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.webjars.NotFoundException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    @ExceptionHandler({BindException.class, NotFoundException.class })
    public String getErrorPath(Model model, final Exception e) {
        model.addAttribute(new ErrorResponse(e.getLocalizedMessage()));
        return "error";
    }
}
