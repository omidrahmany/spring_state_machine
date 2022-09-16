package omid.ro.state_machine.services;

import lombok.RequiredArgsConstructor;
import omid.ro.state_machine.entities.PaymentEntity;
import omid.ro.state_machine.enums.PaymentEvent;
import omid.ro.state_machine.enums.PaymentState;
import omid.ro.state_machine.repositories.PaymentRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public final static String PAYMENT_ID_HEADER = "payment_id";
    private final PaymentRepository repository;
    private final StateMachineFactory<PaymentState, PaymentEvent> factory;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

    @Override
    public PaymentEntity saveNewPayment(PaymentEntity paymentEntity) {

        paymentEntity.setState(PaymentState.NEW);
        return repository.save(paymentEntity);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.PRE_AUTHORIZE);
        return stateMachine;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_APPROVED);

        return stateMachine;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(paymentId);
        sendEvent(paymentId, stateMachine, PaymentEvent.AUTH_DECLINED);
        return stateMachine;
    }

    @Override
    public Optional<PaymentEntity> getPaymentById(Long paymentId) {
        return repository.findById(paymentId);
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) {
        Optional<PaymentEntity> paymentEntity = repository.findById(paymentId);
        StateMachine<PaymentState, PaymentEvent> stateMachine = factory.getStateMachine(Long.toString(paymentId));
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
            sma.resetStateMachineReactively(new DefaultStateMachineContext<>(
                    paymentEntity.get().getState(), null, null, null));
        });
        stateMachine.start();
        return stateMachine;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event) {
        Message<PaymentEvent> messageEvent = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();
        stateMachine.sendEvent(messageEvent);

    }

}
