package datastructures.algorithms.binarysearch;

public class BinarySearchInSortedIntegerArray
{
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9,10};
        int index = binarySearch(arr, 8);
        System.out.println(index);
    }

    static int binarySearch(int[] arr, int searchNum){
        int left = 0;
        int right = arr.length-1;
        while(left <= right){
            int mid = left + (right - left)/2;//Since low or left keeps changing, we need to calculate mid based on new left and NOT right/2 directly
            if(arr[mid] == searchNum){
                return mid;
            }
            if(arr[mid] < searchNum){
                left = mid+1;
            }
            else
                right = mid - 1;
        }
        return -1;
    }
}
