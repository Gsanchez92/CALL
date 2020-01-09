package com.CALL.CALLapp.dispatcher;

import com.CALL.CALLapp.model.Call;
import com.CALL.CALLapp.model.Empleado;
import com.CALL.CALLapp.model.common.Estado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private Boolean active;

    private ExecutorService executorService;

    private CallCreator callCreator;

    private ConcurrentLinkedDeque<Empleado> empleados;

    public static ConcurrentLinkedDeque<Call> proximasLlamadas = new ConcurrentLinkedDeque<>();


    public Dispatcher(List<Empleado> empleados, Boolean callGenerateState) {

        this.empleados = new ConcurrentLinkedDeque<>(empleados);
        this.callCreator = new CallCreator(callGenerateState);
        this.executorService = Executors.newFixedThreadPool(empleados.size() + 1);
    }

    public static synchronized void dispatchCall(Call call) {
        logger.info("Nueva llamada numero: " + call.getNumeroLLamada());
        proximasLlamadas.add(call);
    }

    public synchronized void start() {
        this.active = true;
        this.executorService.execute(this.callCreator);

        for (Empleado empleado : this.empleados) {
            this.executorService.execute(empleado);
        }
    }

    public synchronized Boolean getActive() {
        return active;
    }
    public Empleado buscarEmpleado(Collection<Empleado> empleadoList) {

        Comparator<Empleado> compararPorNombre = Comparator.comparing(Empleado::getNombreEmpleado);

        Comparator<Empleado> compararPorPrioridad = (Empleado e1, Empleado e2) -> Integer
                .compare(e2.getTipoEmpleado().getPrioridad(), e1.getTipoEmpleado().getPrioridad());

        Comparator<Empleado> comparator = compararPorPrioridad.thenComparing(compararPorNombre);

        Optional<Empleado> comparacionEmpleado = empleadoList.stream().filter(e -> e.getEstadoEmpleado() == Estado.DISPONIBLE)
                .sorted(comparator).findFirst();


        if (!comparacionEmpleado.isPresent()) {
            return null;
        }
        return comparacionEmpleado.get();
    }
    public void run() {
        while (getActive()) {
            if (proximasLlamadas.isEmpty()) {
                continue;
            } else {
                Empleado empleado = buscarEmpleado(this.empleados);
                if (empleado == null) {
                    continue;
                }
                try {
                    Call call = proximasLlamadas.pollFirst();
                    empleado.responder(call);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }

}
