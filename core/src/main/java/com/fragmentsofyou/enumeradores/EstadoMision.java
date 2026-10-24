package com.fragmentsofyou.enumeradores;

public enum EstadoMision {

    HABLAR_CON_ABUELA("Objetivo: Habla con tu abuela"),
    IR_A_DORMIR("Objetivo: anda a dormir wachin"),
    EXPLORAR("Objetivo: Investiga los ruidos extraños"),
    COMBATE_Y_BUSQUEDA("Objetivo: Enfrenta al enemigo y encuentra el fragmento..."),
    BUSCAR_SALIDA("Objetivo: Busca la salida hacia la siguiente zona"),
    EXPLORAR_PASILLO("Objetivo: Explora el pasillo"),
    FASES_FINALES("Objetivo: Investiga los alrededores y busca una salida");

    private final String textoObjetivo;

    EstadoMision(String textoObjetivo) {
        this.textoObjetivo = textoObjetivo;
    }

    public String getTextoObjetivo() {
        return textoObjetivo;
    }
}
