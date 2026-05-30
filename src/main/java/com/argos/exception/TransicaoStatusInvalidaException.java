package com.argos.exception;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }

    public TransicaoStatusInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
