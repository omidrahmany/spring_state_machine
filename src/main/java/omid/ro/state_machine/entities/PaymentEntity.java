package omid.ro.state_machine.entities;

import omid.ro.state_machine.enums.PaymentState;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentState state;

    private BigDecimal amount;

    public PaymentEntity() {
    }

    public PaymentEntity(PaymentState state, BigDecimal amount) {
        this.state = state;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentEntity{" +
                "id=" + id +
                ", state=" + state +
                ", amount=" + amount +
                '}';
    }
}
