package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiscountAssigner {
    private final List<AbstractDiscount> discounts;

    public DiscountAssigner(){
        discounts = Stream.of(
                new GrainDiscount(),
                new TenPercentDiscount()
        ).collect(Collectors.toList());
    }

    public Receipt assignDiscounts(Receipt receipt){
        Receipt resultReceipt = receipt;
        for (AbstractDiscount discount : this.discounts){
            resultReceipt = discount.apply(resultReceipt);
        }

        return resultReceipt;
    }
}
