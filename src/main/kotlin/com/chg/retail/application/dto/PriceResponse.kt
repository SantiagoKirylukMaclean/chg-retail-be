package com.chg.retail.application.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class PriceResponse(
    val productId: Long,
    val brandId: Long,
    val priceList: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val price: BigDecimal,
    val currency: String
)