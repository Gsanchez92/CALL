package com.CALL.CALLapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Call {

    private Integer callDuration;
    private Integer numeroLlamada;
    public static int total = 0;

    public Call(Integer callDuration) {
        total++;
        numeroLlamada = total;
        this.callDuration = callDuration;
    }


    public Integer getCallDuration() {
        return callDuration;
    }


    public String getNumeroLLamada(){return numeroLlamada.toString();}
    public static Call randomCall() {
        return new Call(ThreadLocalRandom.current().nextInt(5, 10 + 1));
    }

    public static List<Call> generateRandomCalls(Integer listSize) {
        List<Call> callList = new ArrayList<Call>();
        for (int i = 0; i < listSize; i++) {
            callList.add(randomCall());
        }
        return callList;
    }
}

