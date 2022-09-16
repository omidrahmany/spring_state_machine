package omid.ro.state_machine.services;

import omid.ro.state_machine.entities.PaymentEntity;
import omid.ro.state_machine.enums.PaymentEvent;
import omid.ro.state_machine.enums.PaymentState;
import omid.ro.state_machine.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    PaymentEntity paymentEntity;

    @BeforeEach
    void setUp() {
        paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(new BigDecimal("12.99"));
    }

    @Test
    void preAuth() {
        PaymentEntity savedPayment = paymentService.saveNewPayment(this.paymentEntity);
        System.out.println(">>>>>>>>>>>>>>>>>> SHOULD BE NEW ");
        System.out.println(savedPayment.getState());
        StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.preAuth(savedPayment.getId());
        Optional<PaymentEntity> preAuthedPayment = paymentService.getPaymentById(savedPayment.getId());
        System.out.println(">>>>>>>>>>>>>>>>>> SHOULD BE PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(stateMachine.getState().getId());
        System.out.println(preAuthedPayment.get());

    }
}