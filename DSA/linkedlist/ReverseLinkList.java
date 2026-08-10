package linkedlist;


public class ReverseLinkList {
    public static void main(String[] args) {
        ListNode head = getLinkList(10);
        System.out.print("Before reversing: ");
        printList(head);
        head = reverseList(head);
        System.out.println();
        System.out.print("After reversing:  ");
        printList(head);
    }

    private static void printList(ListNode head) {
        ListNode temp =head;
        while (temp!=null){
            System.out.print(temp.data+" ");
            temp=temp.next;
        }
    }

    private static ListNode reverseList(ListNode head) {
        ListNode current=head;

        ListNode prev= null,next = null;
        while(current!=null){
            next = current.next;
            current.next=prev;
            prev = current;
            current = next;
        }
        head=prev;
        //return head;
        return head;
    }

    private static ListNode getLinkList(int size) {
        ListNode head = new ListNode(1);
        ListNode temp = head;
        for(int i=2; i<=size;i++){
            temp.next=new ListNode(i);
            temp=temp.next;
        }
        return head;
    }
}
