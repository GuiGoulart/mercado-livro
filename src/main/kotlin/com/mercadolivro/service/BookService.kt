package com.mercadolivro.service

import com.mercadolivro.enums.BookSatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.extension.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) =
        bookRepository.save(book)


    fun findAll(pageable: Pageable): Page<BookModel> =
        bookRepository.findAll(pageable)


    fun findActives(pageable: Pageable): Page<BookModel> =
        bookRepository.findByStatus(BookSatus.ATIVO, pageable)


    fun findById(id: Int): BookModel =
        bookRepository.findById(id).orElseThrow { NotFoundException(Errors.ML1001.message.format(id), Errors.ML1001.code) }


    fun delete(id: Int): BookModel {
        val book = findById(id)

        book.status = BookSatus.CANCELADO
        update(book)
        return book
    }

    fun update(book: BookModel): BookModel =
        bookRepository.save(book)

    fun deleteByCustomer(customer: CustomerModel) {
        val books = bookRepository.findByCustomer(customer)
        for(book in books){
            book.status = BookSatus.DELETADO
        }
        bookRepository.saveAll(books)
    }
}
