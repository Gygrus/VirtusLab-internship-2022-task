package com.virtuslab.internship.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;


@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldCorrectlyAssignGrainDiscountAndNotTenPercentDiscount() throws Exception {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = cereals.price().multiply(BigDecimal.valueOf(7)).multiply(BigDecimal.valueOf(0.85));

        for (int i = 0; i < 7; i++){
            cart.addProduct(cereals);
        }

        // Then
        mvc.perform(MockMvcRequestBuilders
                .post("/createReceipt")
                .content(asJsonString(cart))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(expectedTotalPrice.stripTrailingZeros().toPlainString()));
    }

    @Test
    void shouldCorrectlyAssignTenPercentDiscountAndNotGrainDiscount() throws Exception {
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


        // Then
        mvc.perform(MockMvcRequestBuilders
                        .post("/createReceipt")
                        .content(asJsonString(cart))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(expectedTotalPrice));

    }

    @Test
    void shouldNotAssignAnyDiscounts() throws Exception {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var pork = productDb.getProduct("Pork");
        var cereals = productDb.getProduct("Cereals");
        var expectedTotalPrice = pork.price().add(cereals.price());


        cart.addProduct(pork);

        cart.addProduct(cereals);

        // Then
        mvc.perform(MockMvcRequestBuilders
                        .post("/createReceipt")
                        .content(asJsonString(cart))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(expectedTotalPrice));

    }

    @Test
    void shouldAssignBothDiscounts() throws Exception {
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


        // Then
        mvc.perform(MockMvcRequestBuilders
                        .post("/createReceipt")
                        .content(asJsonString(cart))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(expectedTotalPrice.stripTrailingZeros()));

    }

}
