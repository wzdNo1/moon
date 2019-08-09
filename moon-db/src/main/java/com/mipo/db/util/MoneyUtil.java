package com.mipo.db.util;

import java.text.DecimalFormat;

public class MoneyUtil {
    private static DecimalFormat format = new DecimalFormat("0.##");

    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13 <br/>1670 -> 16 <br/>1705 ->17</b>
     * @return
     */
    public static long toLong(long money){
        return money/100;
    }

    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13 <br/>1670 -> 16 <br/>1705 ->17</b>
     * @return
     */
    public static String toLongStr(long money){
        return String.valueOf(toLong(money));
    }

    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13 <br/>1670 -> 17 <br/>1705 ->17</b>
     * @return
     */
    public static long toLongRound(long money){
        return Math.round(money/100D);
    }

    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13 <br/>1670 -> 17 <br/>1705 ->17</b>
     * @return
     */
    public static String toLongRoundStr(long money){
        return String.valueOf(toLongRound(money));
    }
    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13.0 <br/>1670 -> 16.7 <br/>1705 ->17.05</b>
     * @return
     */
    public static double toDouble(long money){
        return money/100D;
    }
    /**
     * 转换为 元的单位 <br/> <b>ex: 1300 -> 13.00 <br/>1670 -> 16.70 <br/>1705 ->17.05</b>
     * @return
     */
    public static String toDoubleStr(long money){
        return format.format(toDouble(money));
    }

}
