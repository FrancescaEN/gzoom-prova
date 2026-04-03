package it.mapsgroup.gzoom.security;

import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.security.model.SecurityConfiguration;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Generates and validates Json Web Tokens (JWT).
 *
 * @author Fabio G. Strozzi
 */
@Service
public class JwtService {
    private static final Logger LOG = getLogger(JwtService.class);
    private static final String ISSUER = "lmm";
    private static final String AUDIENCE = "end-user";

    private final RsaJsonWebKey rsaJsonWebKey;
    private final SecurityConfiguration config;
    private final JwtPayloadMapper payloadMapper;

    @Autowired
    public JwtService(SecurityConfiguration configuration, JwtPayloadMapper payloadMapper) throws JoseException {
        this.config = configuration;
        this.payloadMapper = payloadMapper;
        this.rsaJsonWebKey = initRsaJsonWebKey();
    }


    protected static RsaJsonWebKey initRsaJsonWebKey() throws JoseException {
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId("k1");
        return rsaJsonWebKey;
    }


    /**
     * Generates the JWT for a certain user.
     *
     * @param user The user profile
     * @return The JWT
     * @throws JoseException
     */
    public String generate(UserLogin user) throws JoseException {
        JwtClaims claims = build();
        claims.setSubject(user.getUsername());
        copyPayload(user, claims);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    private void copyPayload(UserLogin user, JwtClaims claims) {
        Map<String, Object> map = payloadMapper.map(user);
        map.forEach((k, v) -> {
            // quietly skips null values
            if (v == null)
                return;
            if (v instanceof String) {
                claims.setStringClaim(k, (String) v);
            } else if (v instanceof Date) {
                NumericDate nd = NumericDate.fromMilliseconds(((Date) v).getTime());
                claims.setNumericDateClaim(k, nd);
            } else if (v instanceof Instant) {
                NumericDate nd = NumericDate.fromMilliseconds(((Instant) v).toEpochMilli());
                claims.setNumericDateClaim(k, nd);
            } else if (v instanceof Map || v instanceof Number || v instanceof Boolean || v instanceof Collection) {
                claims.setClaim(k, v);
            } else {
                throw new IllegalArgumentException(
                        format("Cannot set claims of unsupported type [key=%s, type=%s]",
                                k, v.getClass().toString()));
            }
        });
    }

    private JwtClaims build() {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(ISSUER);
        claims.setAudience(AUDIENCE);
        if (config.getTokenExpiryMinutes() > 0)
            claims.setExpirationTimeMinutesInTheFuture(config.getTokenExpiryMinutes());
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        return claims;
    }

    /**
     * Validates a JWT token.
     *
     * @param jwt The plain token w/o any Bearer or whatever.
     * @return The username of the principal declared in the payload.
     * @throws BadCredentialsException if validation fails.
     */
    public String validate(String jwt) {
        JwtClaims claims = validateImpl(jwt);
        return payloadMapper.getUsername(claims.getClaimsMap());
    }

    public Long getActiveNode(String jwt) {
        JwtClaims claims = validateImpl(jwt);
        return payloadMapper.getActiveNodeId(claims.getClaimsMap());
    }

    private JwtClaims validateImpl(String jwt) {
        JwtConsumerBuilder jwtConsumerBuilder = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setExpectedIssuer(ISSUER)
                .setExpectedAudience(AUDIENCE)
                .setVerificationKey(rsaJsonWebKey.getKey());
        if (config.getTokenExpiryMinutes() > 0)
            jwtConsumerBuilder = jwtConsumerBuilder.setRequireExpirationTime();

        JwtConsumer jwtConsumer = jwtConsumerBuilder.build();

        try {
            return jwtConsumer.processToClaims(jwt);
        } catch (InvalidJwtException e) {
            LOG.error("Invalid authentication token: {}", e);
            throw new BadCredentialsException("Invalid authentication token");
        }
    }
}
