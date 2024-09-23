package com.devbabys.shoppingmall.Repository

import com.devbabys.shoppingmall.Entity.Order
import com.devbabys.shoppingmall.Entity.ProductCategory
import com.devbabys.shoppingmall.Entity.User
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface OrderRepo: JpaRepository<Order, Long> {
    // 단 건 조회
    fun findByOrderId(orderId: Long): Order

    // 유저 조회
    fun findByUserId(userId: User, pageable: Pageable): List<Order>

    // 최근 날짜 순으로 주문 조회
    fun findByUserIdOrderByOrderIdDesc(userId: User, pageable: Pageable): List<Order>

    // 기간별 주문 조회
     fun findByUserIdAndOrderedAtBetween(
        userId: User,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime,
        pageable: Pageable
    ): List<Order>

    // 주문 상태 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderState = :orderState, o.txId = :txId WHERE o.orderId = :orderId")
    fun updateOrderStateAndTxIdById(
        @Param("orderId") orderId: Long,
        @Param("txId") txId: String?,
        @Param("orderState") orderState: Int
    ): Int

    // 유저 주문 건수 조회
    fun countByUserId(userId: User): Long
}