package com.example.waiterapp.pagamento.pagamentocomdinheiro;

import com.example.waiterapp.pagamento.IPagamento;
import com.example.waiterapp.pagamento.Pagamento;
import com.example.waiterapp.enums.Estado;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class PagamentoComDinheiro extends Pagamento implements IPagamento {

    public PagamentoComDinheiro() {
    }

    public PagamentoComDinheiro(Long id, Estado estadoPagamento, LocalDateTime dataPagamento) {
        super(id, estadoPagamento, dataPagamento);
    }

    @Override
    public Integer confirmarPagamento() {
        return null;
    }
}

