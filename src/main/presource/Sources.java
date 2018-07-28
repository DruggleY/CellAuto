package main.presource;

public class Sources {
    public static Source[] list = null;
    public final int amount=6;
    public Sources(){
        list = new Source[20];
        // Kok's galaxy
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
        // Glider
        list[1] = new Source("Glider","B3/S23");
        list[1].data = new String[]{
                "010",
                "001",
                "111"
        };
        list[1].addpx("25px", new Point(12,12));
        list[1].addpx("15px", new Point(22,22));
        list[1].addpx("10px", new Point(32,32));
        list[1].addpx("5px",  new Point(72, 72));
        // Exploder
        list[2] = new Source("Exploder", "B3/S23");
        list[2].data = new String[]{
                "10101",
                "10001",
                "10001",
                "10001",
                "10101"
        };
        list[2].addpx("25px", new Point(12,12));
        list[2].addpx("15px", new Point(22,22));
        list[2].addpx("10px", new Point(32,32));
        list[2].addpx("5px",  new Point(72, 72));
        //Spaceship
        list[3] = new Source("Spaceship","B3/S23");
        list[3].data = new String[]{
                "01111",
                "10001",
                "00001",
                "10010"
        };
        list[3].addpx("25px", new Point(12,12));
        list[3].addpx("15px", new Point(22,22));
        list[3].addpx("10px", new Point(32,32));
        list[3].addpx("5px",  new Point(72, 72));
        //Tumbler
        list[4] = new Source("Tumbler","B3/S23");
        list[4].data = new String[]{
                "0110110",
                "0110110",
                "0010100",
                "1010101",
                "1010101",
                "1100011"
        };
        list[4].addpx("25px", new Point(12,12));
        list[4].addpx("15px", new Point(22,22));
        list[4].addpx("10px", new Point(32,32));
        list[4].addpx("5px",  new Point(72, 72));
        //高斯帕
        list[5] = new Source("Gosper Glider","B3/S23");
        list[5].addpx("15px",new Point(5,5));
        list[5].addpx("10px",new Point(10,10));
        list[5].addpx("5px",new Point(30,30));
        list[5].data = new String[]{
                "0000000000000000000000001",
                "0000000000000000000000101",
                "000000000000110000001100000000000011",
                "000000000001000100001100000000000011",
                "1100000000100000100011",
                "1100000000100010110000101",
                "0000000000100000100000001",
                "0000000000010001",
                "00000000000011"
        };



    }
}
