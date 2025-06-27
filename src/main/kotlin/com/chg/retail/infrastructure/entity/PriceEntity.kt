package com.chg.retail.infrastructure.entity

import com.chg.retail.domain.model.Price
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "PRICES")
class PriceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "BRAND_ID", nullable = false)
    val brandId: Long,

    @Column(name = "START_DATE", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "END_DATE", nullable = false)
    val endDate: LocalDateTime,

    @Column(name = "PRICE_LIST", nullable = false)
    val priceList: Long,

    @Column(name = "PRODUCT_ID", nullable = false)
    val productId: Long,

    @Column(name = "PRIORITY", nullable = false)
    val priority: Int,

    @Column(name = "PRICE", nullable = false)
    val price: BigDecimal,

    @Column(name = "CURR", nullable = false)
    val currency: String
) {
    // Default constructor required by JPA
    constructor() : this(
        id = null,
        brandId = 0,
        startDate = LocalDateTime.MIN,
        endDate = LocalDateTime.MAX,
        priceList = 0,
        productId = 0,
        priority = 0,
        price = BigDecimal.ZERO,
        currency = ""
    )
    fun toDomain(): Price {
        return Price(
            id = id,
            brandId = brandId,
            startDate = startDate,
            endDate = endDate,
            priceList = priceList,
            productId = productId,
            priority = priority,
            price = price,
            currency = currency
        )
    }

}
