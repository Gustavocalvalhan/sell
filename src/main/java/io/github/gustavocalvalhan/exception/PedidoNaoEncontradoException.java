package io.github.gustavocalvalhan.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido nao Encontrado.");
    }
}
