//package com.justiny.rpete.controller;
//
//import com.justiny.rpete.exceptions.PostNotFoundException;
//import com.justiny.rpete.exceptions.RpeteException;
//import com.justiny.rpete.exceptions.SubredditNotFoundException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.Map;
//
//@ControllerAdvice
//public class RestErrorHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(value =  {RpeteException.class, SubredditNotFoundException.class, PostNotFoundException.class})
//    public ResponseEntity<Object> genericHandler(RuntimeException ex, WebRequest request) {
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        String message = ex.getMessage();
//        parameterMap.put("message", new String[] {message});
//        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }
//}
