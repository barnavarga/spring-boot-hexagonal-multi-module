package com.example.order.domain.service

import com.example.order.application.port.`in`.CourierData
import com.example.order.application.port.`in`.OrderCompletion
import com.example.order.application.port.`in`.CompletionResult
import com.example.order.application.port.out.CourierServicePort
import com.example.order.application.port.out.CustomerDataSourcePort
import com.example.order.application.port.out.OrderPersistencePort
import com.example.order.application.port.out.PaymentSolutionPort
import com.example.order.domain.model.enums.CourierProvider
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SubmitOrderServiceTest {

	private lateinit var orderPersistencePort: OrderPersistencePort
	private lateinit var courierServicePort: CourierServicePort
	private lateinit var customerDataSourcePort: CustomerDataSourcePort
	private lateinit var paymentSolutionPort: PaymentSolutionPort

	private lateinit var submitOrderService: SubmitOrderService

	@BeforeEach
	fun before() {
		orderPersistencePort = mock()
		courierServicePort = mock()
		paymentSolutionPort = mock()
		customerDataSourcePort = mock()

		submitOrderService = SubmitOrderService(
			orderPersistencePort = orderPersistencePort,
			courierServicePort = courierServicePort,
			customerDataSourcePort = customerDataSourcePort,
			paymentSolutionPort = paymentSolutionPort
		)
	}

	@Test
	fun invalidCourierData__submitOrder__failed() {
		// GIVEN
		val orderRequest = mock<OrderCompletion>()
		whenever(orderRequest.courierData).thenReturn(
			CourierData(
				courier = CourierProvider.DHL, usePrimaryAddress = false, address = null
			)
		)
		// WHEN
		val response: Result<CompletionResult> = submitOrderService.submit(orderRequest)

		// THEN
		verify(customerDataSourcePort, times(1)).fetchCustomerById(orderRequest.customerId)
		assertTrue(response.isFailure)
	}
}