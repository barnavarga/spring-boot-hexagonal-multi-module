package com.example.order.application.port.out

import com.example.order.domain.model.Courier
import com.example.order.domain.model.enums.CourierProvider
import com.example.order.domain.service.SubmitOrderService

interface CourierServicePort {
	fun fetchCourierByTag(courier: CourierProvider): Result<Courier>
	fun postShippingRequest(shippingRequest: SubmitOrderService.ShippingRequest): Result<Unit>
}
