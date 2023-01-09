package io.github.gustavocalvalhan.domain.repository;

import io.github.gustavocalvalhan.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntemsPedido extends JpaRepository<ItemPedido, Integer> {
}
