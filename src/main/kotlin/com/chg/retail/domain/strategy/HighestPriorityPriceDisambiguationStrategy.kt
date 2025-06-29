package com.chg.retail.domain.strategy

import com.chg.retail.domain.model.Price
import org.springframework.stereotype.Component

/**
 * Implementation of PriceDisambiguationStrategy that selects the price with the highest priority.
 * Higher numerical value of priority means higher precedence.
 */
@Component
class HighestPriorityPriceDisambiguationStrategy : PriceDisambiguationStrategy {
    
    /**
     * Selects the price with the highest priority from a list of applicable prices.
     * If the list is empty, returns null.
     *
     * @param prices List of applicable prices
     * @return The price with the highest priority, or null if the list is empty
     */
    override fun selectPrice(prices: List<Price>): Price? {
        return prices.maxByOrNull { it.priority }
    }
}