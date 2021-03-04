package com.cj.lottery.event.listener;

import com.cj.lottery.event.model.PayEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayListener {

    @EventListener(classes = PayEvent.class)
    public void onApplicationEvent(PayEvent event) {



    }
}
