package com.clanz.base.util;

import lombok.experimental.UtilityClass;
import software.amazon.awssdk.utils.StringUtils;

@UtilityClass
public class PropertyUtils {

    public String getProperty(String key) {
        try {
            final var value = System.getenv(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
            return System.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }
}
