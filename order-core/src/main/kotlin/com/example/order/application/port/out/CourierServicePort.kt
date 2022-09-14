package com.example.order.application.port.out

import com.example.order.domain.model.Address
import com.example.order.domain.model.Courier
import com.example.order.domain.model.enums.CourierProvider
import com.example.order.domain.util.CustomerId

interface CourierServicePort {
	fun fetchCourierByTag(courier: CourierProvider): Result<Courier>
	fun postShippingRequest(shippingRequest: ShippingRequest): Result<Unit>

	data class ShippingRequest(
		val courierData: CourierProvider, val id: CustomerId, val address: Address
	)
}
