package com.devbabys.shoppingmall.Service

import com.devbabys.shoppingmall.DTO.Authentication.AuthenticationResponse
import com.devbabys.shoppingmall.DTO.Order.OrderRequest
import com.devbabys.shoppingmall.Entity.Order
import com.devbabys.shoppingmall.Entity.OrderDetail
import com.devbabys.shoppingmall.Entity.Product
import com.devbabys.shoppingmall.Repository.OrderDetailRepo
import com.devbabys.shoppingmall.Repository.OrderRepo
import com.devbabys.shoppingmall.Repository.ProductRepo
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService @Autowired constructor(
    private val jwtService: JwtService,
    private val productService: ProductService,
    private val orderRepo: OrderRepo,
    private val orderDetailRepo: OrderDetailRepo,
    private val productRepo: ProductRepo
    //private val shippingAddressService: ShippingAddressService,
    //private val orderItemService: OrderItemService,
) {
    @Transactional
    fun addOrder(authResponse: AuthenticationResponse,
                 orderRequest: OrderRequest): Triple<String, String, Any>
    {
        try {
            val user = jwtService.extractedUserInfo(authResponse)

            // 주문 정보 생성
            var order = orderRepo.save(Order(userId = user))

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
            orderRepo.save(order)

            // 결과값 재구성
            val result = mapOf(
                "orderId" to order.orderId,
                "price" to order.totalPrice,
                "orderName" to "${order.orderId} - ${user.username}님의 주문건"
            )

            return Triple("success", "addOrder", result)

        } catch (e: Exception) {
            return Triple("fail", "addOrder", "program error : $e")
        }
    }

    fun completeOrder(orderRequest: OrderRequest): Triple<String, String, String> {
        val orderId = orderRequest.paymentId!!.substring(22)

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

        return Triple("success", "completeOrder", "test")
    }

}