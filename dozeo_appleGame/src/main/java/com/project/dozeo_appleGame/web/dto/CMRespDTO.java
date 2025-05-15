package com.project.dozeo_appleGame.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CMRespDTO<T> {

    private int code;
    private String message;
    private T data;
}
