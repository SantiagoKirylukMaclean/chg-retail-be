package com.chg.retail.infrastructure.repository

import com.chg.retail.infrastructure.entity.PriceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface JpaPriceRepository : JpaRepository<PriceEntity, Long> {

    @Query("""
        SELECT p FROM PriceEntity p 
        WHERE p.brandId = :brandId 
        AND p.productId = :productId 
        AND :date BETWEEN p.startDate AND p.endDate 
        ORDER BY p.priority DESC 
        LIMIT 1
    """)
    fun findTopByBrandIdAndProductIdAndDateBetweenOrderByPriorityDesc(
        @Param("brandId") brandId: Long,
        @Param("productId") productId: Long,
        @Param("date") date: LocalDateTime
    ): PriceEntity?

    @Query("""
        SELECT p FROM PriceEntity p 
        WHERE p.brandId = :brandId 
        AND p.productId = :productId 
        AND :date BETWEEN p.startDate AND p.endDate 
        ORDER BY p.priority DESC
    """)
    fun findByBrandIdAndProductIdAndDateBetween(
        @Param("brandId") brandId: Long,
        @Param("productId") productId: Long,
        @Param("date") date: LocalDateTime
    ): List<PriceEntity>

    fun findByBrandIdAndProductIdOrderByPriorityDesc(
        brandId: Long,
        productId: Long
    ): List<PriceEntity>
}
