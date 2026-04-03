package it.memelabs.gn.security;

import java.util.regex.Pattern;

/**
 * Created by Andrea Fossi on 18/06/15.
 */
public class PasswordValidator {
    public static class Response {
        private boolean isValid;
        private String message;

        private Response(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }
    }

    public Response validate(PasswordConfiguration conf, String password) {
        boolean isValid = true;
        StringBuffer sb = new StringBuffer();
        if (!checkMinLength(password, conf.minLength)) {
            isValid = false;
            sb.append("length=").append(password.length()).append(" when minLength=").append(conf.minLength).append(" or contains < >, ");

        }
        if (!checkMinUpperCase(password, conf.minUpperCaseChar)) {
            isValid = false;
            sb.append("upperCase=").append(countUpperCase(password)).append(" when minUpperCaseChar=").append(conf.minUpperCaseChar).append(", ");

        }
        if (!checkMinLowerCase(password, conf.minLowerCaseChar)) {
            isValid = false;
            sb.append("lowerCaseChar=").append(countLowerCase(password)).append(" when minLowerCaseChar=").append(conf.minLowerCaseChar).append(", ");

        }
        if (!checkDigit(password, conf.minNumericChar)) {
            isValid = false;
            sb.append("numericChar=").append(countDigit(password)).append(" when minNumericChar=").append(conf.minNumericChar).append(", ");
        }

        if (!checkSpecialChar(password, conf.minSpecialChar)) {
            isValid = false;
            sb.append("specialChar=").append(countSpecialChar(password)).append(" when minSpecialChar=").append(conf.minSpecialChar).append(", ");

        }
        if (!checkForbiddenChar(password)) {
            isValid = false;
            sb.append("contains forbidden chars < >, ");
        }
        String substring = (sb.length() > 0) ? sb.substring(0, sb.length() - 2) : "";
        return new Response(isValid, substring);
    }

    public boolean checkMinLength(String password, int minLength) {
        if (minLength > 0) {
            String passwordRegexp = "[^<>]{" + minLength + ",}";
            return Pattern.compile(passwordRegexp).matcher(password).matches();
        } else {
            return true;
        }
    }

    public boolean checkMinUpperCase(String password, int minUpperCase) {
        if (minUpperCase > 0) {
            return countUpperCase(password) >= minUpperCase;
        } else {
            return true;
        }
    }

    public boolean checkMinLowerCase(String password, int minLowerCase) {
        if (minLowerCase > 0) {
            return countLowerCase(password) >= minLowerCase;
        } else {
            return true;
        }
    }

    public boolean checkDigit(String password, int minDigits) {
        if (minDigits > 0) {
            return countDigit(password) >= minDigits;
        } else {
            return true;
        }
    }

    public boolean checkSpecialChar(String password, int minSpecialChar) {
        if (minSpecialChar > 0) {
            return countSpecialChar(password) >= minSpecialChar;
        } else {
            return true;
        }
    }

    public boolean checkForbiddenChar(String password) {
        return !password.contains("<") && !password.contains(">");
    }


    private int countUpperCase(String str) {
        int z = str.length();
        int ret = 0;
        for (int y = 0; y < z; y++) {
            if (Character.isUpperCase(str.charAt(y))) {
                ret++;
            }
        }
        return ret;
    }

    private int countLowerCase(String str) {
        int z = str.length();
        int ret = 0;
        for (int y = 0; y < z; y++) {
            if (Character.isLowerCase(str.charAt(y))) {
                ret++;
            }
        }
        return ret;
    }

    private int countDigit(String str) {
        int z = str.length();
        int ret = 0;
        for (int y = 0; y < z; y++) {
            if (Character.isDigit(str.charAt(y))) {
                ret++;
            }
        }
        return ret;
    }

    private int countSpecialChar(String str) {
        return str.replaceAll("\\w", "").length();
     /*   int z = str.length();
        int ret = 0;
        for (int y = 0; y < z; y++) {
            if (!Character.isLetterOrDigit(str.charAt(y))) {
                ret++;
            }
        }
        return ret;*/
    }


    /**
     * Should be used for test only.
     * <p/>
     * Default: (8,0,0,0,0,-1,-1)
     * A2A: (8,1,1,1,1,90,3)
     *
     * @param args
     */
    public static void main(String[] args) {
        PasswordValidator val = new PasswordValidator();
        PasswordConfiguration conf1 = new PasswordConfiguration("8,0,0,0,0,-1,-1");
        assertTrue(conf1.isValid);
        assertEquals(conf1.minLength, 8);
        assertEquals(conf1.minUpperCaseChar, 0);
        assertEquals(conf1.minLowerCaseChar, 0);
        assertEquals(conf1.minNumericChar, 0);
        assertEquals(conf1.minSpecialChar, 0);
        assertEquals(conf1.expirationDays, -1);
        assertEquals(conf1.maxFailedLogins, -1);
        assertFalse(val.validate(conf1, "aaa").isValid());
        System.out.println(val.validate(conf1, "aaa").getMessage());
        assertTrue(val.validate(conf1, "aaaaaaaa").isValid());

        PasswordConfiguration conf3 = new PasswordConfiguration("8,1,2,3,4,-5,-6");
        assertTrue(conf3.isValid);
        assertEquals(conf3.minLength, 8);
        assertEquals(conf3.minUpperCaseChar, 1);
        assertEquals(conf3.minLowerCaseChar, 2);
        assertEquals(conf3.minNumericChar, 3);
        assertEquals(conf3.minSpecialChar, 4);
        assertEquals(conf3.expirationDays, -5);
        assertEquals(conf3.maxFailedLogins, -6);
        System.out.println(val.validate(conf3, "aaA123òà$").getMessage());
        assertTrue(val.validate(conf3, "aaA123òà$%").isValid());

        PasswordConfiguration conf2 = new PasswordConfiguration("8,1,1,1,1,90,3");
        assertTrue(conf2.isValid);
        assertEquals(conf2.minLength, 8);
        assertEquals(conf2.minUpperCaseChar, 1);
        assertEquals(conf2.minLowerCaseChar, 1);
        assertEquals(conf2.minNumericChar, 1);
        assertEquals(conf2.minSpecialChar, 1);
        assertEquals(conf2.expirationDays, 90);
        assertEquals(conf2.maxFailedLogins, 3);
        assertFalse(val.validate(conf2, "aaa").isValid());
        System.out.println(val.validate(conf2, "aaa").getMessage());
        assertFalse(val.validate(conf2, "aaaaaaaa").isValid());
        assertFalse(val.validate(conf2, "aaaaaa<aa").isValid());
        System.out.println(val.validate(conf2, "aaaaaa<aa").getMessage());
        assertTrue(val.validate(conf2, "aaA2éft56").isValid());


    }

    private static void assertEquals(int a, int b) {
        if (a != b) throw new RuntimeException(a + "!=" + b);
    }

    private static void assertEquals(String a, String b) {
        if (a == null && b == null) return;
        if (a == null && b != null) throw new RuntimeException(a + " != " + b);
        if (a.equals(b)) throw new RuntimeException(a + " != " + b);
    }

    private static void assertTrue(boolean value) {
        if (!value) throw new RuntimeException("input value is false");
    }

    private static void assertFalse(boolean value) {
        if (value) throw new RuntimeException("input value is true");
    }
}
