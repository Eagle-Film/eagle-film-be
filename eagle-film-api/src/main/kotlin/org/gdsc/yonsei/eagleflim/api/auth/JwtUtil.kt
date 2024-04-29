package org.gdsc.yonsei.eagleflim.api.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object JWTUtil {
	private const val ONE_DAY = 1000L * 60L * 60L * 24L
	private const val SEVEN_DAYS = 1000L * 60L * 60L * 24L * 7L
	private const val SECRET_KEY = "cedaef5988ed68bf483056f090ac308358db5c5765397a945258ac86e354a389"

	const val ACCESS_TOKEN_EXPIRATION_TIME = ONE_DAY
	const val REFRESH_TOKEN_EXPIRATION_TIME = SEVEN_DAYS

	private val signingKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))
		?: throw IllegalStateException("Token을 발급하기 위한 Key가 적절하게 생성되지 않음")

	fun generateToken(
		userId: String,
		expirationInMillisecond: Long
	): String {
		val now = Date()
		val expiration = Date(now.time + expirationInMillisecond)
		val claims = generateClaims(now, expiration)
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(userId)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(signingKey, SignatureAlgorithm.HS256)
			.compact()
	}

	private fun generateClaims(now: Date, expiration: Date): Map<String, String> {
		val nowLocalDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault())
		val expirationLocalDateTime = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault())

		val mapper = ObjectMapper()
		mapper.registerModule(JavaTimeModule())
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		return mapOf(
			"issuedAt" to mapper.writeValueAsString(nowLocalDateTime),
			"expiredAt" to mapper.writeValueAsString(expirationLocalDateTime),
		)
	}

	fun getUserIdFromToken(token: String): String {
		return Jwts.parserBuilder()
			.setSigningKey(signingKey)
			.build()
			.parseClaimsJws(token)
			.body
			.subject
	}
}
