package com.example.luigi.exceptions;

public class KoersClientException extends RuntimeException{
    public KoersClientException(String message){
        super(message);
    }

    public KoersClientException(String message, Exception oorzaak){
        super(message, oorzaak);
    }
}
