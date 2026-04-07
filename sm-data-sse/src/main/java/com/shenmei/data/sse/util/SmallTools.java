package com.shenmei.data.sse.util;

import java.util.Random;

public class SmallTools {

    public static String setRamdomOrderSide(){
        int side=new Random().nextInt(2) + 1;
        return String.valueOf(side);
    }
}
