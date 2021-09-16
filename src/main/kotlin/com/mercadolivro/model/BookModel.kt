package com.mercadolivro.model

import com.mercadolivro.enums.BookSatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.extension.BadRequestException
import org.hibernate.Hibernate
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "book")
data class BookModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel? = null

) {

    @Column
    @Enumerated(EnumType.STRING)
    var status: BookSatus? = null
        set(value){
            if(field == BookSatus.CANCELADO || field == BookSatus.DELETADO){
                throw BadRequestException(Errors.ML1002.message.format(field), Errors.ML1002.code)
            }
            field = value
        }

    constructor(id: Int? = null,
                name: String,
                price: BigDecimal,
                customer: CustomerModel? = null,
                status: BookSatus?): this(id, name, price, customer){
                    this.status = status
                }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BookModel

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , price = $price , customer = $customer , status = $status )"
    }
}