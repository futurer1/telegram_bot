package com.mikhail.telegram.utils;

import org.hashids.Hashids;

public class CryptoTool {

    private final int MIN_HASH_LENGTH = 10;
    private final Hashids hashids;

    public CryptoTool(String salt) {
        this.hashids = new Hashids(salt, MIN_HASH_LENGTH);
    }

    public String hashOf(Long v) {
        return hashids.encode(v);
    }

    public Long idOf(String v) {
        long[] res = hashids.decode(v);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
