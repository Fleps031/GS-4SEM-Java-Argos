package com.argos.exception;

public class MissaoNaoAtivaException extends RuntimeException {
    public MissaoNaoAtivaException(String mensagem) {
        super(mensagem);
    }
}