package com.example.order.application.port.out

import com.example.order.domain.model.Customer
import com.example.order.domain.model.enums.PaymentMethod
import com.example.order.domain.util.OrderId

interface PaymentSolutionPort {
	fun initiatePayment(paymentRequest: PaymentRequest): Result<PaymentInitiationResponse>
	data class PaymentRequest(val orderId: OrderId, val paymentMethod: PaymentMethod, val customer: Customer)
	data class PaymentInitiationResponse(val paymentUrl: String)
}
