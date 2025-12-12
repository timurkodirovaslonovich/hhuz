package com.example.DemoOpenFeign


import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import jakarta.persistence.CascadeType
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

import java.util.Date


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity (

    //id: Long?
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    //created date: Date?
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    //modified date: Date?
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,

    //created by: String?
    @CreatedBy var createdBy: String? = null,
    //last modified by: String?
    @LastModifiedBy var lastModifiedBy: String? = null,
    //deleted: Boolean = false
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false

)



@Entity
class Employer(
    @Column(name = "hh_id", unique = true, nullable = false)
    val hhId: String,
    var name: String?
): BaseEntity()


@Entity
class Vacancy(
    @Column(name = "hh_id", nullable = false, unique = true)
    val hhId: String,
    val name: String?,
    val url: String?,
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "employer_id")
    var employer: Employer?,
    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "area_id")
    var area: AreaEntity? = null

): BaseEntity()

@Entity
@Table(name = "areas")
data class AreaEntity(
    @Column(name = "hh_id", nullable = false, unique = true)
    val hhId: String,

    val name: String? = null,
    val url: String? = null
) : BaseEntity()

@Entity
@Table(name = "telegram_users")
data class TelegramUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val telegramId: Long,

    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val photoUrl: String?,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var lastLoginAt: LocalDateTime = LocalDateTime.now()
)
