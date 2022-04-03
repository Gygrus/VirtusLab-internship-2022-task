package com.virtuslab.internship.app;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.DiscountAssigner;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiptController {

    @PostMapping(value = "/createReceipt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Receipt createReceipt(@RequestBody Basket basket){
        var receiptGenerator = new ReceiptGenerator();
        var discountAssigner = new DiscountAssigner();
        var receipt = receiptGenerator.generate(basket);
        receipt = discountAssigner.assignDiscounts(receipt);
        return receipt;
    }
}
