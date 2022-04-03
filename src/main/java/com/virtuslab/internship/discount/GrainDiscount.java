package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;


public class GrainDiscount extends AbstractDiscount {

    public GrainDiscount(){
        this.discountValue = 0.85;
        NAME  = "GrainDiscount";
    }

    @Override
    protected boolean shouldApply(Receipt receipt) {
        int sum = receipt.entries().stream().filter(receiptEntry ->
                receiptEntry.product().type().compareTo(Product.Type.GRAINS) == 0).mapToInt(ReceiptEntry::quantity).sum();

        return sum > 2 && !receipt.discounts().contains("TenPercentDiscount");
    }

}
