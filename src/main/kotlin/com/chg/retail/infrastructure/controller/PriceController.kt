package com.chg.retail.infrastructure.controller

import com.chg.retail.application.dto.PriceQueryRequest
import com.chg.retail.application.dto.PriceResponse
import com.chg.retail.application.service.PriceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/prices")
@Tag(name = "Prices", description = "API for managing product prices")
class PriceController(private val priceService: PriceService) {

    @Operation(
        summary = "Get applicable price",
        description = "Retrieves the applicable price for a product at a specific date and brand"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Price found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = PriceResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "No applicable price found"
            )
        ]
    )
    @GetMapping
    fun getApplicablePrice(
        @Parameter(description = "Application date in ISO format (yyyy-MM-dd'T'HH:mm:ss)")
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) applicationDate: LocalDateTime,
        @Parameter(description = "Product ID")
        @RequestParam("productId") productId: Long,
        @Parameter(description = "Brand ID")
        @RequestParam("brandId") brandId: Long
    ): ResponseEntity<PriceResponse> {
        val query = PriceQueryRequest(
            applicationDate = applicationDate,
            productId = productId,
            brandId = brandId
        )

        return priceService.findApplicablePrice(query)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}
