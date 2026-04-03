package it.mapsgroup.gzoom.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtil {
    private static final String SEPARATOR = "|";

    public static final List<String> stringTokenizer(String str) {
        if (StringUtils.isEmpty(str))
            return Collections.emptyList();
        List<String> ret = new ArrayList<>();
        String[] tokens = str.split("\\|");
        for (String token : tokens) {
            if (StringUtils.isNotBlank(token))
                ret.add(token);
        }
        return ret;
    }

    public static final String listToString(List<String> iban) {
        if (iban == null || iban.isEmpty())
            return null;
        return iban.stream().collect(Collectors.joining(SEPARATOR, SEPARATOR, SEPARATOR));
    }

}
