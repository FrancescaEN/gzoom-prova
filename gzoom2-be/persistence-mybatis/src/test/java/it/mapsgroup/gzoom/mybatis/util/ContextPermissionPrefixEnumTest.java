package it.mapsgroup.gzoom.mybatis.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContextPermissionPrefixEnumTest {

    @Test
    void getByCode_withValidValue() {
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByCode(ContextPermissionPrefixEnum.CTX_BS.getCode());
        assertNotNull(context);
        assertEquals(context, ContextPermissionPrefixEnum.CTX_BS);
    }

    @Test
    void getByCode_withNoValidValue() {
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByCode("");
        assertNull(context);
    }

    @Test
    void getByPermissionPrefix_withValidValue() {
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByPermissionPrefix(ContextPermissionPrefixEnum.CTX_BS.getPermissionPrefix());
        assertNotNull(context);
        assertEquals(context, ContextPermissionPrefixEnum.CTX_BS);
    }

    @Test
    void getByPermissionPrefix_withNoValidValue() {
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByPermissionPrefix("");
        assertNull(context);
    }
}