package org.example.bookstoreproject.service.utility;

public class ArrayStringParser {

    public static String[] getArrElements(String input) {

        if (input == null || input.length() <= 1) {
            return null;
        }
        input = input.substring(1, input.length() - 1);

        String[] arr = input.split(",");

        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].trim();
                while (arr[i].startsWith("\"") || arr[i].endsWith("\"")|| arr[i].startsWith("'") || arr[i].endsWith("'")) {
                    if (arr[i].startsWith("'") || arr[i].startsWith("\""))
                        arr[i] = arr[i].substring(1);

                    if(arr[i].endsWith("'") || arr[i].endsWith("\""))
                        arr[i] = arr[i].substring(0, arr[i].length() - 1);
                    }
                }
            return arr;
            }
        return null;
        }
}
