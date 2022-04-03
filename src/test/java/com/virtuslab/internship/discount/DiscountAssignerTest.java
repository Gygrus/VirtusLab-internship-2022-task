package com.virtuslab.internship.discount;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DiscountAssignerTest {

    @Test
    void shouldCorrectlyAssignGrainDiscountAndNotTenPercentDiscount() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = cereals.price().multiply(BigDecimal.valueOf(7)).multiply(BigDecimal.valueOf(0.85));

        for (int i = 0; i < 7; i++){
            cart.addProduct(cereals);
        }


        var discountAssigner = new DiscountAssigner();
        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);
        var receiptAfterDiscounts = discountAssigner.assignDiscounts(receipt);

        // Then
        assertNotNull(receiptAfterDiscounts);
        assertEquals(1, receiptAfterDiscounts.entries().size());
        assertEquals(expectedTotalPrice, receiptAfterDiscounts.totalPrice());
        assertEquals(1, receiptAfterDiscounts.discounts().size());
    }

    @Test
    void shouldCorrectlyAssignTenPercentDiscountAndNotGrainDiscount() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var pork = productDb.getProduct("Pork");
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = pork.price().multiply(BigDecimal.valueOf(7)).add(cereals.price()).multiply(BigDecimal.valueOf(0.9));

        for (int i = 0; i < 7; i++){
            cart.addProduct(pork);
        }
        cart.addProduct(cereals);


        var discountAssigner = new DiscountAssigner();
        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);
        var receiptAfterDiscounts = discountAssigner.assignDiscounts(receipt);

        // Then
        assertNotNull(receiptAfterDiscounts);
        assertEquals(2, receiptAfterDiscounts.entries().size());
        assertEquals(expectedTotalPrice, receiptAfterDiscounts.totalPrice());
        assertEquals(1, receiptAfterDiscounts.discounts().size());
    }

    @Test
    void shouldNotAssignAnyDiscounts() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var pork = productDb.getProduct("Pork");
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = pork.price().add(cereals.price());


        cart.addProduct(pork);

        cart.addProduct(cereals);


        var discountAssigner = new DiscountAssigner();
        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);
        var receiptAfterDiscounts = discountAssigner.assignDiscounts(receipt);

        // Then
        assertNotNull(receiptAfterDiscounts);
        assertEquals(2, receiptAfterDiscounts.entries().size());
        assertEquals(expectedTotalPrice, receiptAfterDiscounts.totalPrice());
        assertEquals(0, receiptAfterDiscounts.discounts().size());
    }

    @Test
    void shouldAssignBothDiscounts() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var pork = productDb.getProduct("Pork");
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = pork.price().multiply(BigDecimal.valueOf(2)).
                add(cereals.price().multiply(BigDecimal.valueOf(4))).multiply(BigDecimal.valueOf(0.765));


        cart.addProduct(pork);
        cart.addProduct(pork);

        cart.addProduct(cereals);
        cart.addProduct(cereals);
        cart.addProduct(cereals);
        cart.addProduct(cereals);


        var discountAssigner = new DiscountAssigner();
        var receiptGenerator = new ReceiptGenerator();

        // When
        var receipt = receiptGenerator.generate(cart);
        var receiptAfterDiscounts = discountAssigner.assignDiscounts(receipt);

        // Then
        assertNotNull(receiptAfterDiscounts);
        assertEquals(2, receiptAfterDiscounts.entries().size());
        assertEquals(expectedTotalPrice, receiptAfterDiscounts.totalPrice());
        assertEquals(2, receiptAfterDiscounts.discounts().size());
    }
}
