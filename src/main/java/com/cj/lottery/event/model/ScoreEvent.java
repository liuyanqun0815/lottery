package com.cj.lottery.event.model;

import com.cj.lottery.enums.ScoreTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
public class ScoreEvent extends ApplicationEvent {

    private int customerId;

    private int score;
    /**
     * 0 加，1减
     */
    private ScoreTypeEnum type;

    public ScoreEvent(Object source,int customerId, int score, ScoreTypeEnum type) {
        super(source);
        this.customerId = customerId;
        this.score = score;
        this.type = type;
    }
}
