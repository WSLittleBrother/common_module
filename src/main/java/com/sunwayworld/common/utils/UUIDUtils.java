package com.sunwayworld.common.utils;

import java.util.UUID;

/**
 * Created by sunshujie on 2017/6/17.
 */

public class UUIDUtils {

    public static String genReqID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
