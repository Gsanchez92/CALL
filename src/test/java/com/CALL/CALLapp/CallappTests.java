package com.CALL.CALLapp;

import com.CALL.CALLapp.model.Call;
import com.CALL.CALLapp.model.Empleado;
import com.CALL.CALLapp.model.common.Estado;
import com.CALL.CALLapp.model.common.Tipo;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class CallappTests {

	@Test
	void contextLoads() {
	}


	@Test
	public void testRespuestaMultipleEmpleado() throws InterruptedException {
		Empleado empleado = Empleado.generarEmpleado("operador",Tipo.OPERADOR);
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(empleado);
		assertEquals(Estado.DISPONIBLE, empleado.getEstadoEmpleado());
		TimeUnit.SECONDS.sleep(1);
		empleado.responder(Call.randomCall());
		TimeUnit.SECONDS.sleep(10);
		empleado.responder(Call.randomCall());

		assertEquals(Estado.OCUPADO, empleado.getEstadoEmpleado());

		executorService.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(2, empleado.getCalls().size());
	}
	@Test
	public void testRandomCallCreation_AssertNotNull_And_ValidDuration() {
		Integer init = 5;
		Integer max = 10;
		Call call = Call.randomCall();

		assertNotNull(call);
		assertTrue(init <= call.getCallDuration());
		assertTrue(call.getCallDuration() <= max);
	}

	@Test
	public void testEmpleadoDisponibleAtiendeLlamada() throws InterruptedException {
		Empleado empleado = Empleado.generarEmpleado("operator", Tipo.OPERADOR);
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		executorService.execute(empleado);
		empleado.responder(Call.randomCall());

		executorService.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(1, empleado.getCalls().size());
	}
	@Test
	public void testCreacionEmpleado() {
		Empleado empleado = Empleado.generarEmpleado("operator",Tipo.OPERADOR);

		assertNotNull(empleado);
		assertEquals(Tipo.OPERADOR, empleado.getTipoEmpleado());
		assertEquals(Estado.DISPONIBLE, empleado.getEstadoEmpleado());
	}





}
