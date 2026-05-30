package com.argos.exception;

public class OperacaoInvalidaException extends RuntimeException {

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }

    public OperacaoInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
