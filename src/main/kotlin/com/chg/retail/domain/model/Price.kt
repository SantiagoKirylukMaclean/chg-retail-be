package com.chg.retail.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Price(
    val id: Long? = null,
    val brandId: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val priceList: Long,
    val productId: Long,
    val priority: Int,
    val price: BigDecimal,
    val currency: String
) {

}