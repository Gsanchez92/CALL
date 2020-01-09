package com.CALL.CALLapp.model;

import com.CALL.CALLapp.dispatcher.Dispatcher;
import com.CALL.CALLapp.model.common.Estado;
import com.CALL.CALLapp.model.common.Tipo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class Empleado  implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);

    public String nombre;

    private Tipo tipoEmpleado;

    private Estado estadoEmpleado;

    private Call call;

    private ConcurrentLinkedDeque<Call> calls;

    public Empleado(String name, Tipo tipoEmpleado) {
        calls = new ConcurrentLinkedDeque<Call>();
        this.estadoEmpleado = Estado.DISPONIBLE;
        this.nombre = name;
        this.tipoEmpleado = tipoEmpleado;
    }

    public static Empleado generarEmpleado(String name, Tipo tipoEmpleado)
    {
        return new Empleado(name,tipoEmpleado);
    }

    public String getNombreEmpleado() {
        return nombre;
    }

    public Tipo getTipoEmpleado() {
        return tipoEmpleado;
    }

    public synchronized Estado getEstadoEmpleado() {
        return estadoEmpleado;
    }

    private synchronized void setEstadoEmpleado(Estado estadoEmpleado) {
        logger.debug("El empleado " + getNombreEmpleado() + " pasa de estado " + getEstadoEmpleado());
        this.estadoEmpleado = estadoEmpleado;
        logger.debug(" a estado" + getEstadoEmpleado());

    }

    public synchronized List<Call> getCalls() {
        return new ArrayList<>(calls);
    }

    public synchronized void responder(Call call) {
        try {
            this.call = call;
            this.setEstadoEmpleado(Estado.OCUPADO);
            logger.info("El empleado " + getNombreEmpleado()+ " " + getTipoEmpleado() + "esta atendiendo la llamada "+ call.getNumeroLLamada());
        } catch (Exception e) {
            logger.debug("No hay llamadas");
        }
    }

    public void run() {
        logger.info("El empleado " + getNombreEmpleado()+ " " + getTipoEmpleado()+ " esta disponible");

        while (true) {
            if (Estado.OCUPADO.equals(getEstadoEmpleado())) {
                logger.info("El empleado " + getNombreEmpleado()+ " " + getTipoEmpleado() + "Atiende la llamada "+ call.getNumeroLLamada());

                try {
                    TimeUnit.SECONDS.sleep(this.call.getCallDuration());
                } catch (InterruptedException ie) {
                    Dispatcher.proximasLlamadas.addFirst(this.call);
                    logger.info("El empleado " + getNombreEmpleado()+ " " + getTipoEmpleado() + "tuvo un problema con la llamada "+ call.getNumeroLLamada());


                } finally {
                    this.setEstadoEmpleado(Estado.DISPONIBLE);
                    logger.info("El empleado " + getNombreEmpleado()+ " " + getTipoEmpleado() + "finaliza la llamada "+ call.getNumeroLLamada());

                }
                this.calls.add(call);
            }
        }
    }


}