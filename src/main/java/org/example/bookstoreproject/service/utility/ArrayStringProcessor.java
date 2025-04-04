package org.example.bookstoreproject.service.utility;

public class ArrayStringProcessor {

    public static String[] getArrElements(String input) {
        input = input.substring(1, input.length() - 1);
        String[] arr = input.split(",");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].trim(); //
        }
        return arr;
    }
}
