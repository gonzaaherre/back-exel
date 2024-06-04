package com.example.Repository;

import com.example.Entity.Pedido;
import jakarta.persistence.TemporalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByFechaPedidoBetween(@Temporal(TemporalType.DATE) Date fechaDesde, @Temporal(TemporalType.DATE) Date fechaHasta);
}
