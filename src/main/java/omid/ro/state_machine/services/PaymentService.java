package omid.ro.state_machine.services;

import omid.ro.state_machine.entities.PaymentEntity;
import omid.ro.state_machine.enums.PaymentEvent;
import omid.ro.state_machine.enums.PaymentState;
import org.springframework.statemachine.StateMachine;

import java.util.Optional;

public interface PaymentService {

    PaymentEntity saveNewPayment(PaymentEntity paymentEntity);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);

    StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);

    Optional<PaymentEntity> getPaymentById(Long paymentId);
}
