package com.example.springcoditstudy.d0909.attachment;

import lombok.Getter;

@Getter
public class ImageUploadException extends RuntimeException {
    private String errorCode;
    public ImageUploadException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
