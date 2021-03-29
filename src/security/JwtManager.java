package security;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class JwtManager {

	private static final String CLAIM_ROLE = "role";
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
	private static final SecretKey SECRET_KEY = MacProvider.generateKey(SIGNATURE_ALGORITHM);
	private static final TemporalAmount TOKEN_VALIDITY = Duration.ofHours(4L);
	
	/**
	 * Construye el JWT con el usuario y el rol. Retorna el JWT en formato string
	 */
	public String createToken(String subject, String role) {
		final Instant now = Instant.now();
		final Date expiryDateToken = Date.from(now.plus(TOKEN_VALIDITY));
		return Jwts.builder()
				   .setSubject(subject)
				   .claim(CLAIM_ROLE, role)
				   .setExpiration(expiryDateToken)
				   .setIssuedAt(Date.from(now))
				   .signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
				   .compact();
	}
	
	/**
	 * Analiza JTW firmado por JWS. Si reste metodo regresa sin lanzar excepciones, el token es valido
	 */
	public Jws<Claims> parseToken(String compactToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
														SignatureException, IllegalArgumentException {
		return Jwts.parser()
				   .setSigningKey(SECRET_KEY)
				   .parseClaimsJws(compactToken);
	}
		
}
