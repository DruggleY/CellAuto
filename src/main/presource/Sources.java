package main.presource;

public class Sources {
    public static Source[] list = null;
    public static int amount=1;
    public Sources(){
        list = new Source[20];
        list[0] = new Source("Kok's galaxy","B3/S23");
        list[0].data = new String[]{
                "110111111",
                "110111111",
                "110000000",
                "110000011",
                "110000011",
                "110000011",
                "000000011",
                "111111011",
                "111111011"
        };
        list[0].addpx("25px",new Point(10,10));
        list[0].addpx("15px",new Point(18, 18));
        list[0].addpx("10px", new Point(30, 30));
        list[0].addpx("5px", new Point(70, 70));

    }
}
