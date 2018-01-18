package com.codecool.configurations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mate on 2017. 07. 15..
 */
public class Fuck {

    Integer counter = 5;

    public static void main(String[] args) {
        String alma = "saba";
        System.out.println(alma.length());

        System.out.println(133 % 10);


        System.out.println(CalculateReward(20,4));
        //CalculateReward(20,4);
        //CalculateReward(36,27);
    }

    public String getCounter() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        StringBuilder stringBuilder = new StringBuilder(today.format(date) +'_'+ counter);
        counter++;
        System.out.println(stringBuilder);
        return"";
    }

    public static String CalculateReward(int currentPoint, int numberOfJewelry) {
        int freeItem = currentPoint / 5;
        int pointLeft = (currentPoint >= 5) ? currentPoint % 5 : currentPoint;
        int payItem = 0;

        while (currentPoint/5 < numberOfJewelry && freeItem + payItem != numberOfJewelry) {

            if (pointLeft % 5 == 0 && pointLeft != 0) {
                freeItem++;
                pointLeft = 0;

            } else {
                payItem++;
                pointLeft++;
            }

        }
        return String.format("Point Left: %d. Free Item: %d. Pay Item: %d", pointLeft, freeItem, payItem);

    }

}
