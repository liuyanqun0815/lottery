package com.cj.lottery.domain.pay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayAmount {

    private int total;

    private String currency = "CNY";

}
