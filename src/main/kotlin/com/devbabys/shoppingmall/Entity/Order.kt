package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderId: Long = 0, // 주문번호

    @Column(name = "payment_id")
    var paymentId: String? = null, // 결제 ID

    @Column(name = "payment_error_message")
    var paymentErrorMessage: String? = null, // 결제 실패 시 에러 메시지

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userId: User, // 구매자

    @Column(nullable = false)
    var totalPrice: Long? = 0, // 총 가격

    @Column(nullable = false)
    var orderState: Int? = 0, // 주문 상태
    /* 주문 상태 정보
    * 0 : 주문 대기
    * 1 : 결제 완료
    * 2 : 배송 준비 중
    * 3 : 배송 중
    * 4 : 배송 완료
    * 99 : 결제 실패
    * */

    @Column(nullable = true)
    var trackingNo: String? = null, // 운송장 번호

    @Column(name="memo")
    var memo: String? = null, // 요청 사항

    @Column(name="txId")
    var txId: String? = null, // 결제 완료 시 카드사에서 반환하는 결제ID

    @Column(name = "created_at", nullable = false, updatable = false)
    var orderedAt: LocalDateTime = LocalDateTime.now(), // 주문 일자
)