package com.path.tech.auth.repository;

import com.path.tech.auth.model.OtpEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class OtpRepository {
    private final Map<String, OtpEntry> otpEntryMap = new HashMap<String, OtpEntry>();
    public OtpEntry save(OtpEntry otpEntry) {
        otpEntryMap.put(otpEntry.getPhoneNumber(), otpEntry);
        // ideal scenario otpEntry stored in redis/db
        return otpEntry;
    }

    public  OtpEntry findByPhoneNumber(String phoneNumber) {
        log.info(" otpEntryMap : {}",otpEntryMap);
        return otpEntryMap.get(phoneNumber);
    }
}
