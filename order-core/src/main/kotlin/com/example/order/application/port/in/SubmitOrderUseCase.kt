package com.example.order.application.port.`in`

import com.example.order.domain.model.Address
import com.example.order.domain.model.enums.CourierProvider
import com.example.order.domain.model.enums.PaymentMethod
import com.example.order.domain.util.CartId
import com.example.order.domain.util.CustomerId
import com.example.order.domain.util.OrderId

interface SubmitOrderUseCase {

	fun submit(orderRequest: OrderCompletion): Result<OrderRequestCompletion>

	data class OrderCompletion(
		val customerId: CustomerId, val cartId: CartId, val paymentMethod: PaymentMethod, val courierData: CourierData
	)

	data class CourierData(
		val courier: CourierProvider, val usePrimaryAddress: Boolean, val address: Address?
	)

	data class OrderRequestCompletion(
		val orderId: OrderId, val paymentInstruction: String
	)
}


