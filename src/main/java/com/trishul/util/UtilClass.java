package com.trishul.util;

public class UtilClass {

    public static String formatNumber(Double number) {
        if (number < 1000) {
            return String.valueOf(number);
        }

        final String[] units = {"K", "M", "B", "T"};
        int unitIndex = -1;
        double value = number;

        while (value >= 1000 && unitIndex < units.length - 1) {
            value /= 1000;
            unitIndex++;
        }

        return String.format("%.1f%s", value, units[unitIndex])
                .replace(".0", "");
    }
}
