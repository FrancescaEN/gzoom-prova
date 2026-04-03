package it.mapsgroup.gzoom.security.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Andrea Fossi.
 */
public class SecurityDomainObjectTest {

    @Test
    public void name() throws Exception {
        SecurityDomainObject co = SecurityDomainObject.COMPANY;
        assertTrue(co.isRead());
        assertTrue(co.isWrite());
        assertTrue(co.isDelete());
        assertFalse(co.isValidate());
        assertFalse(co.isBasic());

        SecurityDomainObject admin = SecurityDomainObject.ADMIN;
        assertFalse(admin.isRead());
        assertFalse(admin.isWrite());
        assertFalse(admin.isDelete());
        assertFalse(admin.isValidate());
        assertTrue(admin.isBasic());

        SecurityDomainObject auditing = SecurityDomainObject.AUDITING;
        assertTrue(auditing.isRead());
        assertFalse(auditing.isWrite());
        assertFalse(auditing.isDelete());
        assertFalse(auditing.isValidate());
        assertFalse(auditing.isBasic());

    }
}
