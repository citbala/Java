public class TypeCastCheck {
    public static void main(String[] args) {
        System.out.println("5"+3);
        System.out.println("5".repeat(3));
        String str = "5"+3;
        System.out.println("Concatenated String : "+str);
        int i = 34;
        long l =i;
        System.out.printf("Long value : %d, int value : %d %n", l, i);
        //int i1= l; //uncommenting this line will give error as long cannot be converted to int
        double d = 34.5;
        int i2 = (int) d;// if braces removed we do type narrowing and it will give error as double cannot be converted to int
        System.out.println("Int value casted from double : "+i2);
    }
}
