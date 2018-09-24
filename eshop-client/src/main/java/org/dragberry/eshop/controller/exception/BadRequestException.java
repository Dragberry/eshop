package org.dragberry.eshop.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

/**
 * BAD request exception
 * @author Drahun Maksim
 *
 */
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 2216033747515832912L;

    public BadRequestException(String msg) {
    	super(msg);
    }
}
