package com.cj.lottery.domain.pay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payer {

    /**
     * 微信公众号openid
     */
    private String openid;
}
