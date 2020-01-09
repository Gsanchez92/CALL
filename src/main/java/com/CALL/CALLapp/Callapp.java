package com.CALL.CALLapp;

import com.CALL.CALLapp.dispatcher.Dispatcher;
import com.CALL.CALLapp.model.Empleado;
import com.CALL.CALLapp.model.common.Tipo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Callapp {
	private static final Logger logger = LoggerFactory.getLogger(Callapp.class);

	public static void main(String[] args) {

		List<Empleado> empleados = new ArrayList<Empleado>();

		empleados.add(Empleado.generarEmpleado("Gonza", Tipo.DIRECTOR));
		empleados.add(Empleado.generarEmpleado("Martin", Tipo.DIRECTOR));
		empleados.add(Empleado.generarEmpleado("Federico", Tipo.OPERADOR));
		empleados.add(Empleado.generarEmpleado("Luciana", Tipo.OPERADOR));
		empleados.add(Empleado.generarEmpleado("Gabriela", Tipo.SUPERVISOR));
		empleados.add(Empleado.generarEmpleado("Pablo", Tipo.SUPERVISOR));


		Dispatcher dispatcher = new Dispatcher(empleados, true);
		dispatcher.start();

		try {
			TimeUnit.SECONDS.sleep(1);
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(dispatcher);
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException ex) {
			logger.error("Ejecución interrumpida " + ex.getMessage());
		}


	}
}
