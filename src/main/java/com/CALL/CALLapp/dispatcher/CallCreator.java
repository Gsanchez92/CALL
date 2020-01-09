package com.CALL.CALLapp.dispatcher;

import com.CALL.CALLapp.model.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CallCreator  implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CallCreator.class);

    private Boolean active;

    public CallCreator(Boolean callGenerateState) {
        this.active = callGenerateState;
    }

    public synchronized Boolean getActive() {
        return active;
    }

    public synchronized void stop() {
        this.active = false;
    }


    @Override
    public void run() {
        while (getActive()) {
            if(Dispatcher.proximasLlamadas.isEmpty()){
                int totalCallGenerator = ThreadLocalRandom.current().nextInt(1, 15);
                List<Call> calls = Call.generateRandomCalls(totalCallGenerator);
                calls.forEach(call -> {
                    Dispatcher.dispatchCall(call);
                });
            }
        }

    }

}
