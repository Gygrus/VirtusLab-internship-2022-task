package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        HashMap<Product, Integer> productCount = new HashMap<Product, Integer>();
        for (Product product : basket.getProducts()){
            int count = productCount.getOrDefault(product, 0);
            productCount.put(product, count+1);
        }
        productCount.forEach((product, integer) ->
                receiptEntries.add(new ReceiptEntry(product, integer, product.price().multiply(BigDecimal.valueOf(integer)))));

        return new Receipt(receiptEntries);
    }
}
