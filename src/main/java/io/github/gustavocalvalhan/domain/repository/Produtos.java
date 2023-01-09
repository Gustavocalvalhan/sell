package io.github.gustavocalvalhan.domain.repository;

import io.github.gustavocalvalhan.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
