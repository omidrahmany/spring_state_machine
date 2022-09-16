package omid.ro.state_machine.services;

import lombok.RequiredArgsConstructor;
import omid.ro.state_machine.entities.PaymentEntity;
import omid.ro.state_machine.enums.PaymentEvent;
import omid.ro.state_machine.enums.PaymentState;
import omid.ro.state_machine.repositories.PaymentRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository repository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine, StateMachine<PaymentState, PaymentEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(
                    PaymentServiceImpl.PAYMENT_ID_HEADER, -1L))).ifPresent(paymentId -> {
                Optional<PaymentEntity> paymentEntity = repository.findById(paymentId);
                paymentEntity.ifPresent(payment -> {
                    payment.setState(state.getId());
                    repository.save(payment);
                });
            });
        });
    }
}
