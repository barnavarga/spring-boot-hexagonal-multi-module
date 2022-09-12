package com.example.order.application.port.out

import com.example.order.domain.model.Cart
import com.example.order.domain.model.Customer
import com.example.order.domain.util.CartId
import com.example.order.domain.util.CustomerId

interface CustomerDataSourcePort {
	fun fetchCustomerById(customerId: CustomerId): Result<Customer>
	fun fetchCartByCustomerAndCart(customerId: CustomerId, cartId: CartId): Result<Cart>
	fun disableCart(customerId: CustomerId, id: CartId): Result<Unit>
}
