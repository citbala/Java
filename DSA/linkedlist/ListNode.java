package linkedlist;

public class ListNode{
    int data;
    ListNode next;//The value null assigned even before constructor is called
    /*
    The new instruction allocates the object and zeroes the whole memory block.
    That zeroing is what gives int data = 0 and ListNode next = null. It happens before <init> is invoked.
    So the default is set pre-constructor — that part of the comment is correct.
    * */

    /*
    * <init> is the internal name the JVM gives to every constructor.
    * Constructors aren't methods in Java source, but in the class file they're compiled into methods named <init>.
    *
    * ()V — the descriptor: takes no arguments, returns void.
    * Constructors always have return type V in the descriptor, even though new yields an object.
    * The object reference comes from the dup'd reference on the stack, not from a return value.
    *
    * constructor always have invokespecial
    * */
    public ListNode(int data){
        this.data =data;
        this.next=null;
    }
}