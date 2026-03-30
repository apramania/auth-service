package com.apratim.banking.auth_service.exception;

public class InvalidPasswordException extends RuntimeException{

        public InvalidPasswordException() {
            super("Invalid password provided.");
        }
}
