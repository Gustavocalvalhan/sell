package io.github.gustavocalvalhan.service;

import io.github.gustavocalvalhan.domain.entity.Pedido;
import io.github.gustavocalvalhan.domain.enums.StatusPedido;
import io.github.gustavocalvalhan.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar (PedidoDTO dto);
   Optional<Pedido> obterPedidoCompleto(Integer id);
   void atualizaStatus (Integer id, StatusPedido statusPedido);


}
