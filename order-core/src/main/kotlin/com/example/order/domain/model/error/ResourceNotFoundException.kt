package com.example.order.domain.model.error

open class ResourceNotFoundException(
	title: String, detail: String
) : DomainException(errorType = ERROR_TYPE, title = title, detail = detail) {
	companion object {
		const val ERROR_TYPE = "resource-not-found"
	}
}