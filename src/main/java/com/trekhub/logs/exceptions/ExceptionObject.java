package com.trekhub.logs.exceptions;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


//TODO: Check on the two annotations in detail
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionObject {
    private  int httpCode;
    private String errorMessage;
}
