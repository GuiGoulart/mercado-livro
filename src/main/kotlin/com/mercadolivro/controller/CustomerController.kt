package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.model.MessageModel
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customer")
class CustomerController(
    val customerService: CustomerService
) {

    @GetMapping
    fun getAll(@RequestParam name: String?): List<CustomerResponse> {
        return customerService.getAll(name).map { it.toResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody customer: PostCustomerRequest): CustomerResponse {
        return customerService.createCustomer(customer.toCustomerModel()).toResponse()
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): CustomerResponse {
        return customerService.findById(id).toResponse()
    }

    @PutMapping("/{id}")
    fun putCustomer(@PathVariable id: Int, @RequestBody customer: PutCustomerRequest): CustomerResponse {
        val customerSaved = customerService.findById(id)
        return customerService.putCustomer(customer.toCustomerModel(customerSaved)).toResponse()
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Int): MessageModel {
        customerService.deleteByCustomer(id)
        return MessageModel("Usuário deletado com sucesso!")
    }

}