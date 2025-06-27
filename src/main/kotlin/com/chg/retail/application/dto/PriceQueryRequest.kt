package com.chg.retail.application.dto

import java.time.LocalDateTime

data class PriceQueryRequest(
    val applicationDate: LocalDateTime,
    val productId: Long,
    val brandId: Long
)