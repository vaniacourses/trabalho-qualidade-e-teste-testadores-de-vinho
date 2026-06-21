package com.example.waiterapp.item.prato;

import com.example.waiterapp.ingrediente.Ingrediente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para a entidade Prato")
class PratoTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2021, Month.JANUARY, 1, 0, 0);

    private Prato prato;

    @BeforeEach
    void setUp() {
        prato = new Prato(1L, "Frango Grelhado", "Prato saudável com proteína", FIXED_DATE, 25.0);
    }

    // ======================= getCaloriaTotal() =======================

    @Test
    @DisplayName("getCaloriaTotal deve somar corretamente as calorias de múltiplos ingredientes")
    void getCaloriaTotal_comMultiplosIngredientes_deveSomarTodasAsCalorias() {
        // Arrange
        Ingrediente proteina    = new Ingrediente(1L, "Frango", "Proteína magra",  FIXED_DATE, 200.0f);
        Ingrediente carboidrato = new Ingrediente(2L, "Arroz",  "Carboidrato",     FIXED_DATE, 150.0f);
        Ingrediente legume      = new Ingrediente(3L, "Brócolis","Vegetal",         FIXED_DATE,  50.0f);
        prato.setIngredientes(Arrays.asList(proteina, carboidrato, legume));

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(400.0f, caloriasTotal, 0.001f);
    }

    @Test
    @DisplayName("getCaloriaTotal sem ingredientes deve retornar zero (caso limite vazio)")
    void getCaloriaTotal_semIngredientes_deveRetornarZero() {
        // Arrange
        prato.setIngredientes(new ArrayList<>());

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(0.0f, caloriasTotal, 0.001f);
    }

    @Test
    @DisplayName("getCaloriaTotal com um único ingrediente deve retornar suas calorias")
    void getCaloriaTotal_comUmIngrediente_deveRetornarCaloriasDesseIngrediente() {
        // Arrange
        Ingrediente azeite = new Ingrediente(1L, "Azeite", "Gordura saudável", FIXED_DATE, 120.0f);
        prato.setIngredientes(Arrays.asList(azeite));

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(120.0f, caloriasTotal, 0.001f);
    }

    @Test
    @DisplayName("getCaloriaTotal com ingrediente de zero calorias deve manter soma correta")
    void getCaloriaTotal_comIngredienteDeCaloriaZero_deveManterId() {
        // Arrange
        Ingrediente comCaloria = new Ingrediente(1L, "Frango",  "Proteína", FIXED_DATE, 200.0f);
        Ingrediente semCaloria = new Ingrediente(2L, "Água",    "Hidratante", FIXED_DATE,  0.0f);
        prato.setIngredientes(Arrays.asList(comCaloria, semCaloria));

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(200.0f, caloriasTotal, 0.001f);
    }

    @Test
    @DisplayName("getCaloriaTotal com valores decimais deve calcular corretamente")
    void getCaloriaTotal_comValoresDecimais_deveCalcularCorretamente() {
        // Arrange
        Ingrediente i1 = new Ingrediente(1L, "Ing1", "Desc", FIXED_DATE, 33.3f);
        Ingrediente i2 = new Ingrediente(2L, "Ing2", "Desc", FIXED_DATE, 66.7f);
        prato.setIngredientes(Arrays.asList(i1, i2));

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(100.0f, caloriasTotal, 0.01f);
    }

    @Test
    @DisplayName("getCaloriaTotal com grande número de ingredientes deve somar corretamente")
    void getCaloriaTotal_comMuitosIngredientes_deveSomarTodos() {
        // Arrange – 10 ingredientes com 50 calorias cada = 500 calorias
        java.util.List<Ingrediente> ingredientes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ingredientes.add(new Ingrediente((long) i, "Ing" + i, "Desc", FIXED_DATE, 50.0f));
        }
        prato.setIngredientes(ingredientes);

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(500.0f, caloriasTotal, 0.001f);
    }

    @Test
    @DisplayName("getCaloriaTotal com todos ingredientes de caloria alta deve não transbordar")
    void getCaloriaTotal_ingredientesComCaloriaAlta_deveNaoTransbordar() {
        // Arrange
        Ingrediente i1 = new Ingrediente(1L, "IngAlto1", "Desc", FIXED_DATE, 1000.0f);
        Ingrediente i2 = new Ingrediente(2L, "IngAlto2", "Desc", FIXED_DATE, 1000.0f);
        prato.setIngredientes(Arrays.asList(i1, i2));

        // Act
        Float caloriasTotal = prato.getCaloriaTotal();

        // Assert
        assertEquals(2000.0f, caloriasTotal, 0.001f);
    }

    // ======================= Ingredientes =======================

    @Test
    @DisplayName("prato recém criado com construtor padrão deve ter lista de ingredientes vazia")
    void prato_recemCriado_deveTermListaDeIngredientesVazia() {
        // Arrange & Act
        Prato novoPrato = new Prato();

        // Assert
        assertNotNull(novoPrato.getIngredientes());
        assertTrue(novoPrato.getIngredientes().isEmpty());
    }

    @Test
    @DisplayName("setIngredientes deve substituir a lista anterior de ingredientes")
    void setIngredientes_deveSubstituirListaAnterior() {
        // Arrange
        Ingrediente antigo = new Ingrediente(1L, "Antigo", "Desc", FIXED_DATE, 10.0f);
        Ingrediente novo1  = new Ingrediente(2L, "Novo1",  "Desc", FIXED_DATE, 20.0f);
        Ingrediente novo2  = new Ingrediente(3L, "Novo2",  "Desc", FIXED_DATE, 30.0f);
        prato.setIngredientes(Arrays.asList(antigo));

        // Act
        prato.setIngredientes(Arrays.asList(novo1, novo2));

        // Assert
        assertEquals(2, prato.getIngredientes().size());
        assertFalse(prato.getIngredientes().contains(antigo));
    }

    // ======================= Herança de Item =======================

    @Test
    @DisplayName("prato deve herdar o preço definido em Item")
    void prato_deveHerdarPrecoDeItem() {
        assertEquals(25.0, prato.getPreco(), 0.001);
    }

    @Test
    @DisplayName("prato deve herdar o nome definido em Item")
    void prato_deveHerdarNomeDeItem() {
        assertEquals("Frango Grelhado", prato.getNome());
    }

    @Test
    @DisplayName("prato deve herdar o id definido em Item")
    void prato_deveHerdarIdDeItem() {
        assertEquals(1L, prato.getId());
    }

    @Test
    @DisplayName("prato deve herdar a descrição definida em Item")
    void prato_deveHerdarDescricaoDeItem() {
        assertEquals("Prato saudável com proteína", prato.getDescricao());
    }

    @Test
    @DisplayName("setPreco herdado de Item deve funcionar em Prato")
    void setPreco_herdadoDeItem_deveFuncionar() {
        // Arrange & Act
        prato.setPreco(30.0);

        // Assert
        assertEquals(30.0, prato.getPreco(), 0.001);
    }
}
