package com.example.waiterapp.garcom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Garcom")
class GarcomTest {

    private static final LocalDateTime DATA = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Garcom garcom;

    @BeforeEach
    void setUp() {
        garcom = new Garcom(1L, "João Silva", DATA, "11122233344");
    }

    @Test
    @DisplayName("construtor com argumentos deve inicializar todos os campos")
    void construtor_comArgumentos_deveInicializarCampos() {
        assertEquals(1L, garcom.getId());
        assertEquals("João Silva", garcom.getNome());
        assertEquals(DATA, garcom.getDataCriacao());
        assertEquals("11122233344", garcom.getCpf());
    }

    @Test
    @DisplayName("construtor padrão deve criar instância válida")
    void construtor_padrao_deveCriarInstancia() {
        Garcom g = new Garcom();
        assertNotNull(g);
    }

    @Test
    @DisplayName("setters devem atualizar os campos corretamente")
    void setters_devemAtualizarCampos() {
        garcom.setId(2L);
        garcom.setNome("Maria");
        garcom.setCpf("99988877766");
        garcom.setDataCriacao(DATA.plusDays(1));

        assertEquals(2L, garcom.getId());
        assertEquals("Maria", garcom.getNome());
        assertEquals("99988877766", garcom.getCpf());
        assertEquals(DATA.plusDays(1), garcom.getDataCriacao());
    }

    @Test
    @DisplayName("equals deve retornar true para o mesmo objeto")
    void equals_mesmoObjeto_deveRetornarTrue() {
        assertEquals(garcom, garcom);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com null")
    void equals_comparadoComNull_deveRetornarFalse() {
        assertNotEquals(null, garcom);
    }

    @Test
    @DisplayName("equals deve retornar false quando comparado com classe diferente")
    void equals_classeDiferente_deveRetornarFalse() {
        assertNotEquals(garcom, "garçom");
    }

    @Test
    @DisplayName("equals deve retornar true para garcons com mesmo id")
    void equals_mesmoId_deveRetornarTrue() {
        Garcom outro = new Garcom(1L, "Outro Nome", DATA, "00000000000");
        assertEquals(garcom, outro);
    }

    @Test
    @DisplayName("equals deve retornar false para garcons com ids diferentes")
    void equals_idsDiferentes_deveRetornarFalse() {
        Garcom outro = new Garcom(2L, "João Silva", DATA, "11122233344");
        assertNotEquals(garcom, outro);
    }

    @Test
    @DisplayName("equals deve retornar true quando ambos os ids são null")
    void equals_ambosIdsNull_deveRetornarTrue() {
        Garcom a = new Garcom();
        Garcom b = new Garcom();
        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals deve retornar false quando apenas um id é null")
    void equals_apenasUmIdNull_deveRetornarFalse() {
        Garcom comId = new Garcom(1L, "João", DATA, "12345678901");
        Garcom semId = new Garcom();
        assertNotEquals(comId, semId);
    }

    @Test
    @DisplayName("hashCode deve ser igual para garcons com mesmo id")
    void hashCode_mesmoId_deveSerIgual() {
        Garcom outro = new Garcom(1L, "Outro", DATA, "00000000000");
        assertEquals(garcom.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("hashCode deve diferir para ids distintos")
    void hashCode_idsDiferentes_deveDiferir() {
        Garcom outro = new Garcom(2L, "João", DATA, "12345678901");
        assertNotEquals(garcom.hashCode(), outro.hashCode());
    }

    @Test
    @DisplayName("toString deve conter o nome do garçom")
    void toString_deveConterNome() {
        assertTrue(garcom.toString().contains("João Silva"));
    }

    @Test
    @DisplayName("getPedidos e setPedidos devem funcionar corretamente")
    void pedidos_getterESetter_devemFuncionar() {
        garcom.setPedidos(new ArrayList<>());
        assertNotNull(garcom.getPedidos());
        assertTrue(garcom.getPedidos().isEmpty());
    }
}
