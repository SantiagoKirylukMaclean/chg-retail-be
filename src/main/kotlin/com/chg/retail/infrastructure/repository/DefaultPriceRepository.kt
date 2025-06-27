package com.chg.retail.infrastructure.repository

import com.chg.retail.domain.model.Price
import com.chg.retail.domain.repository.PriceRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class DefaultPriceRepository(private val jpaPriceRepository: JpaPriceRepository) : PriceRepository {

    override fun findApplicablePrice(brandId: Long, productId: Long, date: LocalDateTime): Price? {
        return jpaPriceRepository.findTopByBrandIdAndProductIdAndDateBetweenOrderByPriorityDesc(
            brandId = brandId,
            productId = productId,
            date = date
        )?.toDomain()
    }

    override fun findAllApplicablePrices(brandId: Long, productId: Long, date: LocalDateTime): List<Price> {
        return jpaPriceRepository.findByBrandIdAndProductIdAndDateBetween(
            brandId = brandId,
            productId = productId,
            date = date
        ).map { it.toDomain() }
    }

    override fun findByBrandIdAndProductId(brandId: Long, productId: Long): List<Price> {
        return jpaPriceRepository.findByBrandIdAndProductIdOrderByPriorityDesc(
            brandId = brandId,
            productId = productId
        ).map { it.toDomain() }
    }
}
