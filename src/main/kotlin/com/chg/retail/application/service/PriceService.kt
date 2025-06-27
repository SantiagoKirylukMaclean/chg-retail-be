package com.chg.retail.application.service

import com.chg.retail.application.dto.PriceQueryRequest
import com.chg.retail.application.dto.PriceResponse

interface PriceService {

    fun findApplicablePrice(query: PriceQueryRequest): PriceResponse?
}