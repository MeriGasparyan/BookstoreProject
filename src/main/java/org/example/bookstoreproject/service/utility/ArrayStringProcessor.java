package org.example.bookstoreproject.service.utility;

public class ArrayStringProcessor {

    public static String[] getArrElements(String input) {

        if (input == null || input.length() <= 1) {
            return null;
        }
        input = input.substring(1, input.length() - 1);

        String[] arr = input.split(",");

        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].trim();

                if (arr[i].startsWith("'") && arr[i].endsWith("'")) {
                    arr[i] = arr[i].substring(1, arr[i].length() - 1);
                } else if (arr[i].startsWith("\"") && arr[i].endsWith("\"")) {
                    arr[i] = arr[i].substring(1, arr[i].length() - 1);
                }
            }
            System.out.println("Array length: " + arr.length);
            return arr;
        }

        return null;
    }
}
