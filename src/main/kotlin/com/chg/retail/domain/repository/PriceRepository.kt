package com.chg.retail.domain.repository

import com.chg.retail.domain.model.Price
import java.time.LocalDateTime

interface PriceRepository {

    fun findApplicablePrice(brandId: Long, productId: Long, date: LocalDateTime): Price?

    fun findAllApplicablePrices(brandId: Long, productId: Long, date: LocalDateTime): List<Price>
}
