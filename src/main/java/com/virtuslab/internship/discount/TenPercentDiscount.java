package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;

public class TenPercentDiscount extends AbstractDiscount {

    public TenPercentDiscount(){
        this.discountValue = 0.9;
        NAME  = "TenPercentDiscount";
    }

    @Override
    protected boolean shouldApply(Receipt receipt) {
        return receipt.totalPrice().compareTo(BigDecimal.valueOf(50)) > 0;
    }
}
