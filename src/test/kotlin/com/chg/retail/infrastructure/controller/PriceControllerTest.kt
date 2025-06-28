package com.chg.retail.infrastructure.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val dateFormatter = DateTimeFormatter.ISO_DATE_TIME

    @Test
    fun `Test 1 - Request at 10-00 on day 14 for product 35455 and brand 1 (ZARA)`() {
        // Given
        val date = LocalDateTime.of(2020, 6, 14, 10, 0, 0)
        val productId = 35455L
        val brandId = 1L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value(productId))
            .andExpect(jsonPath("$.brandId").value(brandId))
            .andExpect(jsonPath("$.priceList").value(1))
            .andExpect(jsonPath("$.price").value(35.50))
            .andExpect(jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `Test 2 - Request at 16-00 on day 14 for product 35455 and brand 1 (ZARA)`() {
        // Given
        val date = LocalDateTime.of(2020, 6, 14, 16, 0, 0)
        val productId = 35455L
        val brandId = 1L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value(productId))
            .andExpect(jsonPath("$.brandId").value(brandId))
            .andExpect(jsonPath("$.priceList").value(2))
            .andExpect(jsonPath("$.price").value(25.45))
            .andExpect(jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `Test 3 - Request at 21-00 on day 14 for product 35455 and brand 1 (ZARA)`() {
        // Given
        val date = LocalDateTime.of(2020, 6, 14, 21, 0, 0)
        val productId = 35455L
        val brandId = 1L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value(productId))
            .andExpect(jsonPath("$.brandId").value(brandId))
            .andExpect(jsonPath("$.priceList").value(1))
            .andExpect(jsonPath("$.price").value(35.50))
            .andExpect(jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `Test 4 - Request at 10-00 on day 15 for product 35455 and brand 1 (ZARA)`() {
        // Given
        val date = LocalDateTime.of(2020, 6, 15, 10, 0, 0)
        val productId = 35455L
        val brandId = 1L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value(productId))
            .andExpect(jsonPath("$.brandId").value(brandId))
            .andExpect(jsonPath("$.priceList").value(3))
            .andExpect(jsonPath("$.price").value(30.50))
            .andExpect(jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `Test 5 - Request at 21-00 on day 16 for product 35455 and brand 1 (ZARA)`() {
        // Given
        val date = LocalDateTime.of(2020, 6, 16, 21, 0, 0)
        val productId = 35455L
        val brandId = 1L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.productId").value(productId))
            .andExpect(jsonPath("$.brandId").value(brandId))
            .andExpect(jsonPath("$.priceList").value(4))
            .andExpect(jsonPath("$.price").value(38.95))
            .andExpect(jsonPath("$.currency").value("EUR"))
    }

    @Test
    fun `Should return 404 when no price is found`() {
        // Given
        val date = LocalDateTime.of(2023, 1, 1, 10, 0, 0)
        val productId = 99999L
        val brandId = 99999L

        // When & Then
        mockMvc.perform(
            get("/api/prices")
                .param("date", date.format(dateFormatter))
                .param("productId", productId.toString())
                .param("brandId", brandId.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }
}