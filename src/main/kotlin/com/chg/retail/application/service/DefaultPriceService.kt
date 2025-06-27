package com.chg.retail.application.service

import com.chg.retail.application.dto.PriceQueryRequest
import com.chg.retail.application.dto.PriceResponse
import com.chg.retail.domain.model.Price
import com.chg.retail.domain.repository.PriceRepository
import org.springframework.stereotype.Service

@Service
class DefaultPriceService(private val priceRepository: PriceRepository) : PriceService {

    override fun findApplicablePrice(query: PriceQueryRequest): PriceResponse? {
        val applicablePrices = priceRepository.findAllApplicablePrices(
            brandId = query.brandId,
            productId = query.productId,
            date = query.applicationDate
        )

        return applicablePrices
            .maxByOrNull { it.priority }
            ?.toResponse()
    }

    private fun Price.toResponse(): PriceResponse {
        return PriceResponse(
            productId = productId,
            brandId = brandId,
            priceList = priceList,
            startDate = startDate,
            endDate = endDate,
            price = price,
            currency = currency
        )
    }
}
