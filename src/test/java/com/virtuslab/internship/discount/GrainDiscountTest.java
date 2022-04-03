package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrainDiscountTest {

    @Test
    void shouldApplyGrainDiscountWhenThereAreAtLeast3GrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 2));

        var receipt = new Receipt(receiptEntries);
        var discount = new GrainDiscount();
        var expectedTotalPrice = bread.price().add(cereals.price().multiply(BigDecimal.valueOf(2))).multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldNotApplyGrainDiscountWhenThereAreLessThan3GrainProducts() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var banana = productDb.getProduct("Banana");

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));
        receiptEntries.add(new ReceiptEntry(banana, 3));

        var receipt = new Receipt(receiptEntries);
        var discount = new GrainDiscount();
        var expectedTotalPrice = bread.price().add(cereals.price()).add(banana.price().multiply(BigDecimal.valueOf(3)));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldNotApplyGrainDiscountWhenThereWas10PercentDiscountGrantedEarlier() {
        // Given
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var steak = productDb.getProduct("Steak");

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 2));
        receiptEntries.add(new ReceiptEntry(steak, 3));

        var receipt = new Receipt(receiptEntries);
        var grainDiscount = new GrainDiscount();
        var tenPercentDiscount = new TenPercentDiscount();
        var expectedTotalPrice = bread.price().add(cereals.price().multiply(BigDecimal.valueOf(2))).add(steak.price().
                multiply(BigDecimal.valueOf(3))).multiply((BigDecimal.valueOf(0.9)));

        // When
        var receiptAfterDiscount = tenPercentDiscount.apply(receipt);
        receiptAfterDiscount = grainDiscount.apply(receiptAfterDiscount);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }
}
