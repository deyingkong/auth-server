package com.austin.flashcard.auth.controller;

import com.austin.flashcard.auth.exception.UserExistException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.Date;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice {

	private static final String DEFAULT_ERROR_VIEW = "error";


	@ResponseStatus(HttpStatus.NOT_FOUND)  // 409
	@ExceptionHandler(NoResourceFoundException.class)
	public void handleNoResource(HttpServletRequest req, Exception exception) {
		// Nothing to do
		log.error("Request: " + req.getRequestURI() + " raised " + exception);
	}

	@ExceptionHandler({UserExistException.class})
	public ModelAndView handleUserExistError(HttpServletRequest req, Exception exception) throws Exception {

		log.error("Request: " + req.getRequestURI() + " raised " + exception);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL());
		mav.addObject("timestamp", new Date().toString());
		mav.addObject("status", 500);

		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}

	@ExceptionHandler({Exception.class})
	public ModelAndView handleError(HttpServletRequest req, Exception exception) throws Exception {
		log.error("Request:{}, error:{}", req.getRequestURL(), exception.getMessage(), exception);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL());
		mav.addObject("timestamp", new Date().toString());
		mav.addObject("status", 500);

		mav.setViewName(DEFAULT_ERROR_VIEW);
		return mav;
	}

}