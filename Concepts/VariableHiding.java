/*
Variable values gets binded at compile time, we can chck bytecode
m1() is not re-written, just copied after compile time resolution
Thats why variables cant be overridden

Unless we modify Base's field at run time or using super.x, it wont get changed
*/
class Base{
    int x= 10;
    public void m1(){
        System.out.println("Base : m1().x = " +x);
    }
}
class Derived extends Base{
    int x=20;
    public void m2(){
        System.out.println("Inside m2");
        System.out.println("Derived : m2().x = " +x);
        System.out.println("Derived : m2().x + base.x = "+ (x+super.x));
        super.x=100;
        System.out.println("calling m1()");
        m1();
    }
}
public class VariableHiding {
    public static void main(String[] args) {
        Derived d = new Derived();
        d.m1();
        System.out.println("Calling m2() from main");
        d.m2();
        Base b = (Base) d;
        System.out.println("value of x : "+b.x);
        System.out.println("value of x : "+d.x);

    }
}
