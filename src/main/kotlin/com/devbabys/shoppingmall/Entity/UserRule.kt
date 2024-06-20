package com.devbabys.shoppingmall.Entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Entity
@Table(name = "user_rule", indexes = [Index(name="idx_user_rule_user_id", columnList = "user_id", unique = true)])
data class UserRule (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var ruleId : Long = 0, // 권한 식별 ID

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var userId: User, // 유저 식별 ID

    @Column(nullable = false)
    var grade: Int = 4, // 관리 등급 : 1-> 최고 관리자 권한, 2-> 관리자 권한, 3-> 판매자 권한, 4-> 구매자 권한

    @Column(nullable = false)
    var isCreate: Boolean = false, // 쓰기 권한

    @Column(nullable = false)
    var isUpdate: Boolean = false, // 수정 권한

    @Column(nullable = false)
    var isDelete: Boolean = false, // 삭제 권한
)