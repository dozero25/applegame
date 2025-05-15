package com.project.dozeo_appleGame.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class CustomInputException extends RuntimeException{
    private Map<String, String> errorMap;
}
