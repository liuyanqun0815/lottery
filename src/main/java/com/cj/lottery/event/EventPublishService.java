package com.cj.lottery.event;

import com.cj.lottery.enums.ScoreTypeEnum;
import com.cj.lottery.event.model.PayEvent;
import com.cj.lottery.event.model.ScoreEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventPublishService {

    @Autowired
    ApplicationContext applicationContext;

    public void publish(ApplicationEvent event) {
        log.info("EventPublishService publish event {}", event);
        try {
            applicationContext.publishEvent(event);
        } catch (Exception e) {
            log.error("EventPublishService Error", e);
        }
    }

    public void addScore(Object source, int customerId, int score, ScoreTypeEnum type) {
        publish(new ScoreEvent(source, customerId, score, type));
    }

    public void addMoney(Object source, int customerId, int money, ScoreTypeEnum type) {
        publish(new PayEvent(source, customerId, money, type));
    }
}
