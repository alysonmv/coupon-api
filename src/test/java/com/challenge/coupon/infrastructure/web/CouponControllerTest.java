package com.challenge.coupon.infrastructure.web;

import com.challenge.coupon.domain.CouponStatus;
import com.challenge.coupon.infrastructure.web.dto.CouponResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String validPayload() throws Exception {
        return objectMapper.writeValueAsString(Map.of(
            "code", "ABC-123",
            "description", "Test coupon",
            "discountValue", 0.8,
            "expirationDate", LocalDateTime.now().plusDays(30).toString(),
            "published", false
        ));
    }

    @Test
    void shouldReturn201WhenCreatingValidCoupon() throws Exception {
        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("ABC123"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    void shouldReturn400WhenDiscountValueIsInvalid() throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of(
            "code", "ABC123",
            "description", "Test",
            "discountValue", 0.3,
            "expirationDate", LocalDateTime.now().plusDays(30).toString(),
            "published", false
        ));
        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenExpirationDateIsInThePast() throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of(
            "code", "ABC123",
            "description", "Test",
            "discountValue", 1.0,
            "expirationDate", LocalDateTime.now().minusDays(1).toString(),
            "published", false
        ));
        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCodeProducesLessThanSixAlphanumericChars() throws Exception {
        String payload = objectMapper.writeValueAsString(Map.of(
            "code", "AB-1",
            "description", "Test",
            "discountValue", 1.0,
            "expirationDate", LocalDateTime.now().plusDays(30).toString(),
            "published", false
        ));
        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WhenGettingExistingCoupon() throws Exception {
        MvcResult created = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload()))
            .andExpect(status().isCreated())
            .andReturn();

        CouponResponse response = objectMapper.readValue(
            created.getResponse().getContentAsString(), CouponResponse.class);

        mockMvc.perform(get("/coupon/" + response.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.id().toString()));
    }

    @Test
    void shouldReturn404WhenGettingNonExistentCoupon() throws Exception {
        mockMvc.perform(get("/coupon/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn204WhenSoftDeletingCoupon() throws Exception {
        MvcResult created = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload()))
            .andExpect(status().isCreated())
            .andReturn();

        CouponResponse response = objectMapper.readValue(
            created.getResponse().getContentAsString(), CouponResponse.class);

        mockMvc.perform(delete("/coupon/" + response.id()))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentCoupon() throws Exception {
        mockMvc.perform(delete("/coupon/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenDeletingAlreadyDeletedCoupon() throws Exception {
        MvcResult created = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload()))
            .andExpect(status().isCreated())
            .andReturn();

        CouponResponse response = objectMapper.readValue(
            created.getResponse().getContentAsString(), CouponResponse.class);

        mockMvc.perform(delete("/coupon/" + response.id()))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/" + response.id()))
            .andExpect(status().isConflict());
    }
}
