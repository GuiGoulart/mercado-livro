package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.extension.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.MessageModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()
    }

    fun createCustomer(customer: CustomerModel): CustomerModel {
        return customerRepository.save(customer)
    }

    fun findById(id: Int): CustomerModel =
        customerRepository.findById(id).orElseThrow { NotFoundException(Errors.ML1101.message.format(id), Errors.ML1101.code) }


    fun putCustomer(customer: CustomerModel): CustomerModel {
        if(!customerRepository.existsById(customer.id!!)){
            throw Exception()
        }else{
            return customerRepository.save(customer)
        }
    }

    fun deleteByCustomer(id: Int): MessageModel {
        if (!customerRepository.existsById(id)) {
            throw Exception()
        } else {
            val customer = findById(id)
            bookService.deleteByCustomer(customer)
            customer.status = CustomerStatus.INATIVO
            customerRepository.save(customer)
            return MessageModel("Usuário deletado com sucesso!")
        }
    }
}