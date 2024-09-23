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

            val resultMap = mapOf(
                "total" to orderRepo.countByUserId(user),
                "data" to result
            )

            return Triple("success", "getOrderListAll", resultMap)

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

            var totalCnt = 0
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
                totalCnt++
            }

            val resultMap = mapOf(
                "total" to totalCnt,
                "data" to result
            )

            return Triple("success", "getOrderListAll", resultMap)

        } catch (e: Exception) {
            return Triple("fail", "getOrderListAll", "program error : $e")
        }
    }

    fun getOrderDetail(authResponse: AuthenticationResponse, orderId: Long): Triple<String, String, Any> {
        try {
            val orderInfo = orderRepo.findByOrderId(orderId)
            val orderDetailInfo = orderDetailRepo.findByOrderId(orderInfo)

            var detailResult: List<Any> = mutableListOf()
            orderDetailInfo!!.forEach {
                val temp = mapOf(
                    "productId" to it.productId.productId,
                    "productName" to it.productId.name,
                    "quantity" to it.quantity
                )
                detailResult = detailResult + temp
            }

            val result = mapOf(
                "orderId" to orderInfo.orderId,
                "paymentId" to orderInfo.paymentId,
                "totalPrice" to orderInfo.totalPrice,
                "product" to detailResult,
                "orderState" to orderInfo.orderState,
                "trackingNo" to orderInfo.trackingNo,
                "memo" to orderInfo.memo,
                "orderedAt" to orderInfo.orderedAt
            )

            return Triple("success", "getOrderDetail", result)

        } catch (e: Exception) {
            return Triple("fail", "getOrderDetail", "program error : $e")
        }
    }



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
}