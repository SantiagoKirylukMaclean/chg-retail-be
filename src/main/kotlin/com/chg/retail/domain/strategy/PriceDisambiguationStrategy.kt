package com.chg.retail.domain.strategy

import com.chg.retail.domain.model.Price

/**
 * Strategy interface for disambiguating prices when multiple prices are applicable
 * for the same product, brand, and date.
 */
interface PriceDisambiguationStrategy {
    /**
     * Selects the most appropriate price from a list of applicable prices.
     *
     * @param prices List of applicable prices
     * @return The selected price, or null if the list is empty
     */
    fun selectPrice(prices: List<Price>): Price?
}