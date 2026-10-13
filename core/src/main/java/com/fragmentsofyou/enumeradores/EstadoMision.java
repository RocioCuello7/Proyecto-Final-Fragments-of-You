package com.fragmentsofyou.enumeradores;

public enum EstadoMision {

    HABLAR_CON_ABUELA("Objetivo: Habla con tu abuela"),
    IR_A_DORMIR("Objetivo: anda a dormir wachin"),
    EXPLORAR("Objetivo: Investiga los ruidos extraños"),
    COMPLETADO("Objetivo completado!!!"),
    COMBATE_Y_BUSQUEDA("Objetivo: Enfrenta al enemigo y encuentra un fragmento..."),
    BUSQUEDA("Objetivo: ? ? ?");


    private final String textoObjetivo;

    EstadoMision(String textoObjetivo) {
        this.textoObjetivo = textoObjetivo;
    }

    public String getTextoObjetivo() {
        return textoObjetivo;
    }
}
