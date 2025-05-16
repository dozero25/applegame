package com.project.dozeo_appleGame.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class CMRespDTO<T> {
    private int code;
    private String message;
    private T data;
}
