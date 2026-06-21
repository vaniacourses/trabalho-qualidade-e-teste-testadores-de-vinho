package com.example.waiterapp.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para ObjectNotFoundException")
class ObjectNotFoundExceptionTest {

    @Test
    @DisplayName("construtor com mensagem deve preservar a mensagem")
    void construtor_comMensagem_deveManteMensagem() {
        ObjectNotFoundException ex = new ObjectNotFoundException("objeto não encontrado");

        assertEquals("objeto não encontrado", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @DisplayName("construtor com mensagem e causa deve preservar ambos")
    void construtor_comMensagemECausa_deveManteMensagemECausa() {
        Throwable causa = new RuntimeException("causa raiz");
        ObjectNotFoundException ex = new ObjectNotFoundException("objeto não encontrado", causa);

        assertEquals("objeto não encontrado", ex.getMessage());
        assertEquals(causa, ex.getCause());
    }

    @Test
    @DisplayName("deve ser instância de RuntimeException")
    void objectNotFoundException_deveSerRuntimeException() {
        ObjectNotFoundException ex = new ObjectNotFoundException("erro");

        assertInstanceOf(RuntimeException.class, ex);
    }
}
