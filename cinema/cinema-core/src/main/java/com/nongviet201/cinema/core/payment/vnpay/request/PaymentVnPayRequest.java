package com.nongviet201.cinema.core.payment.vnpay.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVnPayRequest {
    Integer billId;
    Long amount;
}
