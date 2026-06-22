package com.example.waiterapp.item.bebida;

import com.example.waiterapp.item.Item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Bebida extends Item {
    @Column(nullable = false)
    private String quantidade;

    public Bebida() {
    }

    public Bebida(Long id, String nome, String descricao, LocalDateTime dataCriacao, Double preco, String quantidade) {
        super(id, nome, descricao, dataCriacao, preco);
        this.quantidade = quantidade;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bebida that = (Bebida) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Bebida{" +
                super.toString() +
                "quantidade='" + quantidade + '\'' +
                '}';
    }
}
