package datastructures.algorithms.window;

import java.util.Arrays;

public class SlidingWindowSum {
    static void main() {
        Integer[] arr = {1, 2, 3, 4, 5};
        Integer k = 3;
        //output = {1,3,6,9,12}
        Integer[] result = new Integer[arr.length];
        int windowSum = 0;
        for(int i = 0; i < arr.length; i++){
            windowSum += arr[i];
            if(i>=k)
                windowSum -= arr[i-k];
            result[i] = windowSum;
        }
        System.out.println(Arrays.toString(result));
    }
}
