package io.github.gustavocalvalhan.service.impl;


import io.github.gustavocalvalhan.domain.entity.Cliente;
import io.github.gustavocalvalhan.domain.entity.ItemPedido;
import io.github.gustavocalvalhan.domain.entity.Pedido;
import io.github.gustavocalvalhan.domain.entity.Produto;
import io.github.gustavocalvalhan.domain.enums.StatusPedido;
import io.github.gustavocalvalhan.domain.repository.Clientes;
import io.github.gustavocalvalhan.domain.repository.IntemsPedido;
import io.github.gustavocalvalhan.domain.repository.Pedidos;
import io.github.gustavocalvalhan.domain.repository.Produtos;
import io.github.gustavocalvalhan.exception.PedidoNaoEncontradoException;
import io.github.gustavocalvalhan.exception.RegraNegocioException;
import io.github.gustavocalvalhan.rest.dto.ItemPedidoDTO;
import io.github.gustavocalvalhan.rest.dto.PedidoDTO;
import io.github.gustavocalvalhan.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRespository;
    private final Produtos produtosRepository;
    private final IntemsPedido itemsPedidoRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRespository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("código de cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        return null;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());


    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioException("não é possivel realizar um pedido sem items.");
        }

        return items
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto invalido: " + idProduto
                                    ));

                    ItemPedido itempedido = new ItemPedido();
                    itempedido.setQuantidade(dto.getQuantidade());
                    itempedido.setPedido(pedido);
                    itempedido.setProduto(produto);
                    return itempedido;

                }).collect(Collectors.toList());

    }
}
