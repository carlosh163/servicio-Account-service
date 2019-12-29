package com.springboot.appbanco.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//https://www.adictosaltrabajo.com/2017/06/29/manejo-de-excepciones-en-springmvc-ii/
//permite declarar métodos relacionados con el manejo de excepciones que serán compartidos entre múltiples controladores
@ControllerAdvice
@RestController
public class ResponseExceptionHandler {
  
  /*@ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> manejarTodasExcepciones(Exception ex, WebRequest request){
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<Object>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }*/

  @ExceptionHandler(ModeloNotFoundException.class)
  public final ResponseEntity<ExceptionResponse> manejarModeloExcepciones(ModeloNotFoundException ex) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage());
 //request.getDescription(false));
    //return new ResponseEntity<Object>(exceptionResponse, HttpStatus.NOT_FOUND);
    
    //return new Mono<ResponseEntity<Object>>(exceptionResponse,HttpStatus.BAD_GATEWAY);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
  }

  
}
