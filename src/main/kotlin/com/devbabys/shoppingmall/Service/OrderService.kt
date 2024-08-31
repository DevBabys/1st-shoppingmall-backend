package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Order.OrderHistoryRequest
import com.devbabys.shoppingmall.DTO.Order.OrderRequest
import com.devbabys.shoppingmall.DTO.Order.OrderResponse
import com.devbabys.shoppingmall.DTO.Order.OrderStateRequest
import com.devbabys.shoppingmall.Entity.Order
import com.devbabys.shoppingmall.Entity.OrderDetail
import com.devbabys.shoppingmall.Repository.OrderDetailRepo
import com.devbabys.shoppingmall.Repository.OrderRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class OrderService @Autowired constructor(
    private val jwtService: JwtService,
    private val orderRepo: OrderRepo,
    private val orderDetailRepo: OrderDetailRepo,
    private val productRepo: ProductRepo,
    @Value("\${portone.store.id}") private val storeId: String,
    @Value("\${portone.channel.key}") private val channelKey: String
) {
    fun getOrderListAll(authResponse: AuthenticationResponse, pageable: Pageable): Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)
            }

            val user = jwtService.extractedUserInfo(authResponse)
            val orderList = orderRepo.findByUserIdOrderByOrderIdDesc(user, customPage)
            var result: List<Any> = mutableListOf()

            orderList.forEach { it ->
                val orderDetail = orderDetailRepo.findFirstByOrderId(it)
                val orderCount = orderDetailRepo.countByOrderId(it)
                val order = mapOf(
                    "orderId" to it.orderId,
                    "orderName" to orderDetail!!.productId.name + " x" +orderDetail.quantity + " 외 " + (orderCount-1) + "건",
                    "paymentId" to it.paymentId,
                    "totalPrice" to it.totalPrice,
                    "orderState" to it.orderState,
                    "trackingNo" to it.trackingNo,
                    "memo" to it.memo,
                    "orderedAt" to it.orderedAt
                )
                result = result + order
            }

            return Triple("success", "getOrderListAll", result)

        } catch (e: Exception) {
            return Triple("fail", "getOrderListAll", "program error : $e")
        }
    }

    fun getOrderListByPeriod(authResponse: AuthenticationResponse,
                             startDate: LocalDate?,
                             endDate: LocalDate?,
                             pageable: Pageable)
    : Triple<String, String, Any> {
        try {
            var customPage = pageable
            if (pageable.pageNumber > 0) {
                customPage = PageRequest.of(pageable.pageNumber - 1, pageable.pageSize)
            }

            val user = jwtService.extractedUserInfo(authResponse)

            // 시작일이 없으면 오류 반환
            if (startDate == null) {
                return Triple("fail", "getOrderListByPeriod", "startDate is null")
            }

            // LocalDate to LocalDateTime
            val tStartDate: LocalDateTime = startDate.atStartOfDay() ?: LocalDate.now().atStartOfDay()
            val tEndDate: LocalDateTime = endDate?.atStartOfDay()?: LocalDate.now().atTime(23,59,59)

            println("### $tStartDate --- $tEndDate")


            val orderList = orderRepo.findByUserIdAndOrderedAtBetween(user, tStartDate, tEndDate, customPage)
            var result: List<Any> = mutableListOf()

            orderList.forEach { it ->
                val orderDetail = orderDetailRepo.findFirstByOrderId(it)
                val orderCount = orderDetailRepo.countByOrderId(it)
                val order = mapOf(
                    "orderId" to it.orderId,
                    "orderName" to orderDetail!!.productId.name + " x" +orderDetail.quantity + " 외 " + (orderCount-1) + "건",
                    "paymentId" to it.paymentId,
                    "totalPrice" to it.totalPrice,
                    "orderState" to it.orderState,
                    "trackingNo" to it.trackingNo,
                    "memo" to it.memo,
                    "orderedAt" to it.orderedAt
                )
                result = result + order
            }

            return Triple("success", "getOrderListAll", result)

        } catch (e: Exception) {
            return Triple("fail", "getOrderListAll", "program error : $e")
        }
    }
    //



    @Transactional
    fun addOrder(authResponse: AuthenticationResponse,
                 orderRequest: OrderRequest): Triple<String, String, Any>
    {
        try {
            val user = jwtService.extractedUserInfo(authResponse)

            // 주문 정보 생성
            val order = orderRepo.save(Order(userId = user))

            // 주문 상세 정보 생성
            var totalPrice: Long = 0
            orderRequest.product.forEach {
                val product = productRepo.findById(it.productId).orElse(null)
                    ?: return Triple("fail", "addOrder", "product not exist")

                if (product.quantity - it.quantity < 0) {
                    return Triple("fail", "addOrder", "not enough product quantity")
                }
                else {
                    // 상품 수량 업데이트
                    product.quantity -= it.quantity
                    productRepo.save(product)

                    // 상품당 주문 상세 정보 생성
                    orderDetailRepo.save(OrderDetail(orderId = order, userId = user, productId = product, quantity = it.quantity))
                    totalPrice += product.price * it.quantity
                }
            }

            // 주문 총액 업데이트
            order.totalPrice = totalPrice

            // 결과값 재구성
            val result = mapOf(
                "storeId" to storeId,
                "channelKey" to channelKey,
                "paymentId" to "devbabys_shop_payment_${order.orderId}",
                "orderName" to "${order.orderId} - ${user.username}님의 주문건",
                "totalAmount" to order.totalPrice,
                "nextUrl" to "order/complete"
            )

            return Triple("success", "addOrder", result)

        } catch (e: Exception) {
            return Triple("fail", "addOrder", "program error : $e")
        }
    }

    fun completeOrder(orderResponse: OrderResponse): Triple<String, String, String> {
        try {
            val orderId = orderResponse.paymentId.substring(22)

            // orderState = 1, 결제 완료 상태로 변경
            orderResponse.txId?.let { orderRepo.updateOrderStateAndTxIdById(orderId.toLong(), it, 1) }
            return Triple("success", "completeOrder", orderId)
        } catch (e: Exception) {
            return Triple("fail", "completeOrder", "program error : $e")
        }
    }

    fun failOrder(orderResponse: OrderResponse): Triple<String, String, String> {
        try {
            val orderId = orderResponse.paymentId.substring(22)
            // orderState = 99, 결제 실패 상태로 변경
            orderRepo.updateOrderStateAndTxIdById(orderId.toLong(), orderResponse.txId, 99)

            return Triple("success", "completeOrder", orderId)
        } catch (e: Exception) {
            return Triple("fail", "completeOrder", "program error : $e")
        }
    }

    fun changeOrderState(orderStateRequest: OrderStateRequest): Triple<String, String, String> {
        try {
            val orderId = orderStateRequest.orderId
            val order = orderRepo.findById(orderId).orElse(null)
                ?: return Triple("fail", "changeOrderState", "orderId not exist")

            order.orderState = orderStateRequest.orderState
            orderRepo.save(order)

            return Triple("success", "changeOrderState", orderId.toString())
        } catch (e: Exception) {
            return Triple("fail", "changeOrderState", "program error : $e")
        }
    }

    fun getOrder(orderResponse: OrderResponse): Triple<String, String, String> {
        try {
            val orderId = orderResponse.paymentId.substring(22)
            

            return Triple("success", "completeOrder", orderId)
        } catch (e: Exception) {
            return Triple("fail", "completeOrder", "program error : $e")
        }
    }

        /*
        *     fetch("/payment/complete", async (req, res) => {
    try {
        // 요청의 body로 paymentId가 전달되기를 기대합니다.
        const { paymentId, orderId } = req.body;

        // 1. 포트원 결제내역 단건조회 API 호출
        const paymentResponse = await fetch(
        `https://api.portone.io/payments/${paymentId}`,
        { headers: { Authorization: `PortOne ${PORTONE_API_SECRET}` } },
        );
        if (!paymentResponse.ok)
        throw new Error(`paymentResponse: ${await paymentResponse.json()}`);
        const payment = await paymentResponse.json();

        // 2. 고객사 내부 주문 데이터의 가격과 실제 지불된 금액을 비교합니다.
        const order = await OrderService.findById(orderId);
        if (order.amount === payment.amount.total) {
        switch (payment.status) {
            case "VIRTUAL_ACCOUNT_ISSUED": {
            // 가상 계좌가 발급된 상태입니다.
            // 계좌 정보를 이용해 원하는 로직을 구성하세요.
            break;
            }
            case "PAID": {
            // 모든 금액을 지불했습니다! 완료 시 원하는 로직을 구성하세요.
            break;
            }
        }
        } else {
        // 결제 금액 불일치, 위/변조 의심
        }
    } catch (e) {
        // 결제 검증 실패
        res.status(400).send(e);
    }});*/
}