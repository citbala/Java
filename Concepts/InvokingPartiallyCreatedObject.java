class BaseL1 {
    BaseL1() { print(); }
    void print() { System.out.println("Base"); }
}
/*
Tricky call to derived method from constructor
* */
class DerivedL2 extends BaseL1 {// Leaking this from partially contructed derived class
    int d1 =3;
    final int d2 = 3;
    final Integer d3 = 4;
    @Override void print() {
        System.out.println("d1 = " + d1);
        System.out.println("d2 = " + d2);
        System.out.println("d3(BoxedInteger) = " + d3);
    }
}

class InvokingPartiallyCreatedObject{
    public static void main(String[] args) {
        BaseL1 b = new DerivedL2();
    }
}