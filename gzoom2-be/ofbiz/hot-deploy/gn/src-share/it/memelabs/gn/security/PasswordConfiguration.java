package it.memelabs.gn.security;

/**
 * Created by Andrea Fossi on 18/06/15.
 */
public class PasswordConfiguration {
    private static String REGEX = "\\d+,\\d+,\\d+,\\d+,\\d+,-?\\d+,-?\\d+";

    protected final int minLength;
    protected final int minUpperCaseChar;
    protected final int minLowerCaseChar;
    protected final int minNumericChar;
    protected final int minSpecialChar;
    protected final int expirationDays;
    protected final int maxFailedLogins;
    protected final boolean isValid;

    public PasswordConfiguration(String value) {
        if (value.matches(REGEX)) {
            String[] values = value.split(",");
            minLength = Integer.parseInt(values[0]);
            minUpperCaseChar = Integer.parseInt(values[1]);
            minLowerCaseChar = Integer.parseInt(values[2]);
            minNumericChar = Integer.parseInt(values[3]);
            minSpecialChar = Integer.parseInt(values[4]);
            expirationDays = Integer.parseInt(values[5]);
            maxFailedLogins = Integer.parseInt(values[6]);
            isValid=true;
        } else {
            minLength = 8;
            minUpperCaseChar = 0;
            minLowerCaseChar = 0;
            minNumericChar = 0;
            minSpecialChar = 0;
            expirationDays = -1;
            maxFailedLogins = -1;
            isValid=false;
        }
    }

    public PasswordConfiguration() {
        minLength = 8;
        minUpperCaseChar = 0;
        minLowerCaseChar = 0;
        minNumericChar = 0;
        minSpecialChar = 0;
        expirationDays = -1;
        maxFailedLogins = -1;
        isValid=true;

    }

    public int getMinLength() {
        return minLength;
    }

    public int getMinUpperCaseChar() {
        return minUpperCaseChar;
    }

    public int getMinLowerCaseChar() {
        return minLowerCaseChar;
    }

    public int getMinNumericChar() {
        return minNumericChar;
    }

    public int getMinSpecialChar() {
        return minSpecialChar;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public int getMaxFailedLogins() {
        return maxFailedLogins;
    }

    public boolean isValid() {
        return isValid;
    }
}

