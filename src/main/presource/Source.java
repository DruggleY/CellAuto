package main.presource;

import java.util.HashMap;
import java.util.Map;

public class Source {
    public Map<String,Point> suitpx = null;
    public String name;
    public String rule;
    public String[] data;
    public Source(String name, String rule)
    {
        this.name = name;
        this.suitpx = new HashMap<String, Point>();
        this.rule=rule;
    }
    public void addpx(String px,Point point)
    {
        this.suitpx.put(px,point);
    }
}
