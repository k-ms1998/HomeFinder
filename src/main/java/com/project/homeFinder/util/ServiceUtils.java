package com.project.homeFinder.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ServiceUtils {

    public static String encodeString(String input) {
        return new String(input.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    public static String checkAndRemoveSubwayNameSuffix(String input) {
        input = ServiceUtils.encodeString(input);
        if(input.endsWith("역")){
            String suffix = ServiceUtils.encodeString("역");
            log.info("Suffix Removed: {} ", input.substring(0, input.length() - suffix.length()));

            return input.substring(0, input.length() - suffix.length());
        }

        return input;
    }

    public static String checkAndRemoveSubwayNameSuffixAndEncode(String input) {
        return encodeString(checkAndRemoveSubwayNameSuffix(input));
    }
}
