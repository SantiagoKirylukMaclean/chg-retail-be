package com.chg.retail.application.service

import com.chg.retail.application.dto.PriceQueryRequest
import com.chg.retail.domain.model.Price
import com.chg.retail.domain.repository.PriceRepository
import com.chg.retail.domain.strategy.PriceDisambiguationStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class DefaultPriceServiceTest {

    @Mock
    private lateinit var priceRepository: PriceRepository

    @Mock
    private lateinit var priceDisambiguationStrategy: PriceDisambiguationStrategy

    private lateinit var priceService: DefaultPriceService

    @BeforeEach
    fun setUp() {
        priceService = DefaultPriceService(priceRepository, priceDisambiguationStrategy)
    }

    @Test
    fun `findApplicablePrice should return the applicable price`() {
        // Given
        val applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0)
        val productId = 35455L
        val brandId = 1L
        val query = PriceQueryRequest(applicationDate, productId, brandId)

        val price = Price(
            id = 2L,
            brandId = brandId,
            startDate = LocalDateTime.of(2020, 6, 14, 15, 0, 0),
            endDate = LocalDateTime.of(2020, 6, 14, 18, 30, 0),
            priceList = 2L,
            productId = productId,
            priority = 1,
            price = BigDecimal("25.45"),
            currency = "EUR"
        )

        val prices = listOf(price)

        `when`(priceRepository.findAllApplicablePrices(brandId, productId, applicationDate))
            .thenReturn(prices)
        `when`(priceDisambiguationStrategy.selectPrice(prices))
            .thenReturn(price)

        // When
        val result = priceService.findApplicablePrice(query)

        // Then
        assertEquals(price.priceList, result?.priceList)
        assertEquals(price.price, result?.price)
    }

    @Test
    fun `findApplicablePrice should return null when no prices are applicable`() {
        // Given
        val applicationDate = LocalDateTime.of(2023, 1, 1, 10, 0, 0)
        val productId = 99999L
        val brandId = 99999L
        val query = PriceQueryRequest(applicationDate, productId, brandId)

        val prices = emptyList<Price>()

        `when`(priceRepository.findAllApplicablePrices(brandId, productId, applicationDate))
            .thenReturn(prices)
        `when`(priceDisambiguationStrategy.selectPrice(prices))
            .thenReturn(null)

        // When
        val result = priceService.findApplicablePrice(query)

        // Then
        assertNull(result)
    }

    @Test
    fun `findApplicablePrice should return the only price when only one price is applicable`() {
        // Given
        val applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0)
        val productId = 35455L
        val brandId = 1L
        val query = PriceQueryRequest(applicationDate, productId, brandId)

        val price = Price(
            id = 1L,
            brandId = brandId,
            startDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            priceList = 1L,
            productId = productId,
            priority = 0,
            price = BigDecimal("35.50"),
            currency = "EUR"
        )

        val prices = listOf(price)

        `when`(priceRepository.findAllApplicablePrices(brandId, productId, applicationDate))
            .thenReturn(prices)
        `when`(priceDisambiguationStrategy.selectPrice(prices))
            .thenReturn(price)

        // When
        val result = priceService.findApplicablePrice(query)

        // Then
        assertEquals(price.priceList, result?.priceList)
        assertEquals(price.price, result?.price)
    }

    @Test
    fun `findApplicablePrice should correctly map Price to PriceResponse`() {
        // Given
        val applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0)
        val productId = 35455L
        val brandId = 1L
        val query = PriceQueryRequest(applicationDate, productId, brandId)

        val price = Price(
            id = 1L,
            brandId = brandId,
            startDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            priceList = 1L,
            productId = productId,
            priority = 0,
            price = BigDecimal("35.50"),
            currency = "EUR"
        )

        val prices = listOf(price)

        `when`(priceRepository.findAllApplicablePrices(brandId, productId, applicationDate))
            .thenReturn(prices)
        `when`(priceDisambiguationStrategy.selectPrice(prices))
            .thenReturn(price)

        // When
        val result = priceService.findApplicablePrice(query)

        // Then
        assertEquals(price.productId, result?.productId)
        assertEquals(price.brandId, result?.brandId)
        assertEquals(price.priceList, result?.priceList)
        assertEquals(price.startDate, result?.startDate)
        assertEquals(price.endDate, result?.endDate)
        assertEquals(price.price, result?.price)
        assertEquals(price.currency, result?.currency)
    }

    @Test
    fun `findApplicablePrice should select the price with highest priority when multiple prices are applicable`() {
        // Given
        val applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0, 0)
        val productId = 35455L
        val brandId = 1L
        val query = PriceQueryRequest(applicationDate, productId, brandId)

        val price1 = Price(
            id = 1L,
            brandId = brandId,
            startDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0),
            endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            priceList = 1L,
            productId = productId,
            priority = 0,
            price = BigDecimal("35.50"),
            currency = "EUR"
        )

        val price2 = Price(
            id = 2L,
            brandId = brandId,
            startDate = LocalDateTime.of(2020, 6, 14, 15, 0, 0),
            endDate = LocalDateTime.of(2020, 6, 14, 18, 30, 0),
            priceList = 2L,
            productId = productId,
            priority = 1,
            price = BigDecimal("25.45"),
            currency = "EUR"
        )

        val prices = listOf(price1, price2)

        `when`(priceRepository.findAllApplicablePrices(brandId, productId, applicationDate))
            .thenReturn(prices)
        `when`(priceDisambiguationStrategy.selectPrice(prices))
            .thenReturn(price2) // The strategy should select the price with higher priority

        // When
        val result = priceService.findApplicablePrice(query)

        // Then
        assertEquals(price2.priceList, result?.priceList)
        assertEquals(price2.price, result?.price)
    }
}
