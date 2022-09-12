package com.example.order.application.port.out

import com.example.order.domain.model.Cart
import com.example.order.domain.util.CartId
import com.example.order.domain.util.CustomerId

interface CartDataSourcePort {
	fun fetchCartByCustomerAndCart(customerId: CustomerId, cartId: CartId): Cart
	fun disableCart(id: CartId)
}
