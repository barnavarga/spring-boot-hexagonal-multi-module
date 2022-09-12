package com.example.order.domain.model.error

open class PersistingOrderException(
	title: String, detail: String, throwable: Throwable
) : DomainException(errorType = ERROR_TYPE, title = title, detail = detail, throwable = throwable) {
	companion object {
		const val ERROR_TYPE = "persisting-order-not-possible"
	}
}