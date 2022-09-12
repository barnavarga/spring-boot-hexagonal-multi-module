package com.example.order.application.port.out

import com.example.order.domain.service.SubmitOrderService

interface PaymentSolutionPort {
	fun initiatePayment(paymentRequest: SubmitOrderService.PaymentRequest): Result<Unit>
}
