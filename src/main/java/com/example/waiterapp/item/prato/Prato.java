package com.example.waiterapp.item.prato;

import com.example.waiterapp.ingrediente.Ingrediente;
import com.example.waiterapp.item.Item;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Prato extends Item {

    @ManyToMany
    @JoinTable(
            name = "PRATO_INGREDIENTE",
            joinColumns = @JoinColumn(name = "fk_prato"),
            inverseJoinColumns = @JoinColumn(name = "fk_ingrediente")
    )
    @OrderBy("nome asc")
    private List<Ingrediente> ingredientes = new ArrayList<>();

    public Prato() {
    }

    public Prato(Long id, String nome, String descricao, LocalDateTime dataCriacao, Double preco) {
        super(id, nome, descricao, dataCriacao, preco);
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Float getCaloriaTotal(){
        return ingredientes.stream().map(Ingrediente::getCaloria).reduce(0F, Float::sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prato that = (Prato) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
