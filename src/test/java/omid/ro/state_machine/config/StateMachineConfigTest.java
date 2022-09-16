package omid.ro.state_machine.config;

import omid.ro.state_machine.enums.PaymentEvent;
import omid.ro.state_machine.enums.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;

@SpringBootTest
class StateMachineConfigTest {


    @Test
    void testNewStateMachine() {

        StateMachineConfigBuilder<PaymentState, PaymentEvent> builder = new StateMachineConfigBuilder<>();
        StateMachineFactory<PaymentState, PaymentEvent> factory = StateMachineFactory.create(builder);

        StateMachine<PaymentState, PaymentEvent> stateMachine = factory.getStateMachine();

        stateMachine.startReactively();
        System.out.println(stateMachine.getState().toString());
        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);
        System.out.println(stateMachine.getState().toString());
        stateMachine.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println(stateMachine.getState().toString());


    }

}