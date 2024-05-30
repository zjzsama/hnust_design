package com.stytle;

import java.awt.*;

public class Fonts {
    public static Font title;
    public static Font account;
    public static Font accounttext;
    public static Font ok;
    public static Font state;
    public Fonts()
    {
        title= new Font("宋体",Font.BOLD,28);
        account=new Font("华文bai行楷",Font.BOLD,18);
        accounttext= new Font("宋体",Font.BOLD,18);
        ok= new Font("宋体",Font.BOLD,18);
        state= new Font("宋体",Font.BOLD,10);
    }
}
