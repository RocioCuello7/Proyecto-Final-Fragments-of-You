package com.fragmentsofyou.enumeradores;

public enum EstadoMision {

    HABLAR_CON_ABUELA("Objetivo: Habla con tu abuela"),
    IR_A_DORMIR("Objetivo: anda a dormir wachin"),
    COMPLETADO("wow");


    private final String textoObjetivo;

    EstadoMision(String textoObjetivo) {
        this.textoObjetivo = textoObjetivo;
    }

    public String getTextoObjetivo() {
        return textoObjetivo;
    }
}
