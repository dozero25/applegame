package com.project.dozeo_appleGame.exception;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class CustomInputSameIdException extends DuplicateRequestException {
    private Map<String, String> errorMap;
}
