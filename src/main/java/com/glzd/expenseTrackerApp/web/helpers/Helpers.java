package com.glzd.expenseTrackerApp.web.helpers;


public class Helpers {
    public static String toSentenceCase(String input){
        String lowerCase = input.toLowerCase();
        return lowerCase.substring(0, 1)
                .toUpperCase()
                .concat(lowerCase
                        .substring(1)
                        .toLowerCase());
    }
}
