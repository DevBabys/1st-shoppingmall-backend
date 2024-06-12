package com.devbabys.shoppingmall.Model

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "userInfo")
data class UserInfo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val userId: User,  // 유저 식별 ID

    var phoneNumber: String? = null, // 연락처

    var zipCode: String? = null, // 우편번호

    var address: String? = null, // 주소

    var detailAddress: String? = null, // 상세주소
)