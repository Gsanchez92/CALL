package com.CALL.CALLapp.model.common;

public enum Tipo {
    OPERADOR(3), SUPERVISOR(2), DIRECTOR(1);


    private int prioridad;

    Tipo(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getPrioridad() {
        return prioridad;
    }


}
