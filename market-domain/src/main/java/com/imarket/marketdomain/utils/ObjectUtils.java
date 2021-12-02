package com.imarket.marketdomain.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Iterator;

public class ObjectUtils {
    private static ObjectMapper objectMapper = null;

    public static String ObjectToJson(Object obj) {
        try {
            if (null == objectMapper) {
                objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
        }
        return "";
    }

    public static boolean isNull(Object s) {
        if (null == s) {
            return true;
        }
        return false;
    }

    public boolean containsIgnoreCase(String[] arr, String word) {
        return -1 < Arrays.binarySearch(arr, word);
    }

    public boolean contains(String[] arr, String word) {
        return -1 < Arrays.binarySearch(arr, word);
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = tokens.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }
}
