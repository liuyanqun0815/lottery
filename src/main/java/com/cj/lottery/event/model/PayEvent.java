package com.cj.lottery.event.model;

import com.cj.lottery.enums.ScoreTypeEnum;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
public class PayEvent extends ApplicationEvent {

    private int customerId;

    private int totalFee;

    private ScoreTypeEnum type;

    public PayEvent(Object source,int customerId, int totalFee, ScoreTypeEnum type) {
        super(source);
        this.customerId = customerId;
        this.totalFee = totalFee;
        this.type = type;
    }
}
