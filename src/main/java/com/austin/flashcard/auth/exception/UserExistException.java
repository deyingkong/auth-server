package com.austin.flashcard.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An example of an application-specific exception. Defined here for convenience
 * as we don't have a real domain model or its associated business logic.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "user is exist")
public class UserExistException extends RuntimeException {
	public UserExistException(String email) {
		super(email + " is exist");
	}
}
