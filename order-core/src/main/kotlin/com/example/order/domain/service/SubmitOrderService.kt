package com.example.order.domain.service

import com.example.order.application.port.`in`.OrderCompletion
import com.example.order.application.port.`in`.CompletionResult
import com.example.order.application.port.`in`.SubmitOrderUseCase
import com.example.order.application.port.out.CourierServicePort
import com.example.order.application.port.out.CustomerDataSourcePort
import com.example.order.application.port.out.OrderPersistencePort
import com.example.order.application.port.out.PaymentSolutionPort
import com.example.order.domain.model.Address
import com.example.order.domain.model.Cart
import com.example.order.domain.model.Courier
import com.example.order.domain.model.CourierInformation
import com.example.order.domain.model.Customer
import com.example.order.domain.model.ErrorReason
import com.example.order.domain.model.Order
import com.example.order.domain.model.enums.OrderStatus
import com.example.order.domain.model.error.PersistingOrderException
import com.example.order.domain.model.error.ResourceNotFoundException
import com.example.order.domain.model.error.ShipmentProviderException
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubmitOrderService(
	private val customerDataSourcePort: CustomerDataSourcePort,
	private val courierServicePort: CourierServicePort,
	private val orderPersistencePort: OrderPersistencePort,
	private val paymentSolutionPort: PaymentSolutionPort
) : SubmitOrderUseCase {

	/**
	 * This function should verify that the order to be submitted could be processed. It also should ensure that the related
	 * services like courier or payment services will be notified about the order and of course it persists the
	 * initiated order.
	 *
	 * @param orderRequest required information to submit an order
	 * @return Order initiation response. It would contain payment instructions concerning the selected payment option.
	 */
	override fun submit(orderRequest: OrderCompletion): Result<CompletionResult> {

		try {
			val customer: Customer = customerDataSourcePort.fetchCustomerById(orderRequest.customerId).getOrElse {
				return Result.failure(
					ResourceNotFoundException(
						"Resource not found!", "Customer not found (${orderRequest.customerId})."
					)
				)
			}

			val shippingRequest = createShippingRequest(orderRequest = orderRequest, customer = customer).getOrElse {
				return Result.failure(it)
			}

			val cart: Cart =
				customerDataSourcePort.fetchCartByCustomerAndCart(orderRequest.customerId, orderRequest.cartId)
					.getOrElse {
						return Result.failure(
							ResourceNotFoundException(
								"Resource not found!",
								"This cart (${orderRequest.cartId}) does not belong to the customer (${orderRequest.customerId})."
							)
						)
					}

			val courier: Courier = courierServicePort.fetchCourierByTag(orderRequest.courierData.courier).getOrElse {
				return Result.failure(
					ResourceNotFoundException(
						"Resource not found!",
						"Courier provider not found by tag (${orderRequest.courierData.courier})."
					)
				)
			}

			val order = Order(
				id = UUID.randomUUID().toString(),
				customer = customer,
				items = cart.items,
				courierData = CourierInformation(courier.id),
				paymentMethod = orderRequest.paymentMethod,
				status = OrderStatus.CREATED
			)
			orderPersistencePort.persist(order = order).getOrElse {
				return Result.failure(
					PersistingOrderException(
						"Persisting order failure!", "Initiating an order is not possible (${orderRequest})", it
					)
				)
			}

			customerDataSourcePort.disableCart(cart.customerId, cart.id)

			courierServicePort.postShippingRequest(shippingRequest).getOrElse {
				val message = "Shipping request couldn't be initiated!"
				orderPersistencePort.updateOrderStatus(
					order.id, OrderStatus.ERROR, ErrorReason(message)
				)
				return Result.failure(ShipmentProviderException("Shipment request error!", message))
			}

			orderPersistencePort.updateOrderStatus(order.id, OrderStatus.PENDING).getOrElse {
				return Result.failure(
					PersistingOrderException(
						"Persisting order failure!", "Updating the order (${order.id}) status is not possible!", it
					)
				)
			}

			val paymentResponse = paymentSolutionPort.initiatePayment(
				PaymentSolutionPort.PaymentRequest(
					orderId = order.id, paymentMethod = orderRequest.paymentMethod, customer = customer
				)
			).getOrElse {
				val message = "Payment request couldn't be initiated!"
				orderPersistencePort.updateOrderStatus(
					order.id, OrderStatus.ERROR, ErrorReason(message)
				)
				return Result.failure(ShipmentProviderException("Payment request error!", message))
			}

			return Result.success(
				CompletionResult(
					orderId = order.id, paymentInstruction = paymentResponse
				)
			)
		} catch (t: Throwable) {
			return Result.failure(t)
		}
	}

	/**
	 * Provide a request object for the shipment of the order.
	 * Courier request information validation: the customer should give an alternative address in case of not using
	 * the primary address option
	 *
	 * @property orderRequest
	 * @return the result of the validation
	 */
	private fun createShippingRequest(
		orderRequest: OrderCompletion, customer: Customer
	): Result<CourierServicePort.ShippingRequest> {
		val address: Address = if (orderRequest.courierData.usePrimaryAddress) {
			customer.address
		} else {
			orderRequest.courierData.address
				?: return Result.failure(IllegalStateException("Address field must not be null!"))
		}

		return Result.success(
			CourierServicePort.ShippingRequest(
				courierData = orderRequest.courierData.courier, id = customer.id, address = address
			)
		)
	}
}