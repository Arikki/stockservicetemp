package com.stockService.error;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stockService.filter.ReqFilter;
import com.stockService.model.ErrorMessage;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	@ExceptionHandler(ExecutionException.class)
	public ResponseEntity<ErrorMessage> executionException(ExecutionException exception, WebRequest request) {

		ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

		ResponseEntity<ErrorMessage> respEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);

		if (exception.getMessage().contains("Index: 0, Size: 0")) {
			message.setHttpStatus(HttpStatus.NOT_FOUND);
			message.setMessage("Company code not found");
			respEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);

		}
		
		logger.info("Exception occured:"+message.toString());

		return respEntity;

	}

}
