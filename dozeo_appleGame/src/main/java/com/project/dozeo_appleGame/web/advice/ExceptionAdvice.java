package com.project.dozeo_appleGame.web.advice;

import com.project.dozeo_appleGame.exception.CustomInputSameIdException;
import com.project.dozeo_appleGame.exception.CustomInputSameNicknameException;
import com.project.dozeo_appleGame.exception.CustomNullCheckInputValueException;
import com.project.dozeo_appleGame.web.dto.CMRespDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CustomInputSameIdException.class)
    public ResponseEntity<?> sameIdError(CustomInputSameIdException e){
        return ResponseEntity.badRequest()
                .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "SameId Error", e.getErrorMap()));
    }

    @ExceptionHandler(CustomInputSameNicknameException.class)
    public ResponseEntity<?> sameNicknameError(CustomInputSameNicknameException e){
        return ResponseEntity.badRequest()
                .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "SameNickname error", e.getErrorMap()));
    }

    @ExceptionHandler(CustomNullCheckInputValueException.class)
    public ResponseEntity<?> nullCheckInputValueError(CustomNullCheckInputValueException e){
        return ResponseEntity.badRequest()
                .body(new CMRespDTO<>(HttpStatus.BAD_REQUEST.value(), "NullInputValue error", e.getErrorMap()));
    }
}
