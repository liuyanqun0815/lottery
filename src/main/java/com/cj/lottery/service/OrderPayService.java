package com.cj.lottery.service;

public interface OrderPayService {

    String createWxOrderPay(int customerId,int totalFee);
}
