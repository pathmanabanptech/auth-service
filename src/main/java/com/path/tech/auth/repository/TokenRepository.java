package com.path.tech.auth.repository;

import com.path.tech.auth.model.RefreshTokenEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class TokenRepository {

    private Map<UUID, RefreshTokenEntity> tokenEntityMap = new HashMap<>();
    public RefreshTokenEntity save(RefreshTokenEntity entity) {
        tokenEntityMap.put(entity.getJti(),entity);
        return findByJtiAndRevokedFalse(entity.getJti());
    }

    public RefreshTokenEntity findByJtiAndRevokedFalse(UUID jti) {
        RefreshTokenEntity entity =  tokenEntityMap.get(jti);
        if(entity.isRevoked()) return null;
        else return entity;
    }
    public RefreshTokenEntity revoke(UUID jti){
        RefreshTokenEntity entity = findByJtiAndRevokedFalse(jti);
        entity.setRevoked(true);
       return save(entity);
    }
}
