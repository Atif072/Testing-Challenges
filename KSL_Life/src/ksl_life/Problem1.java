/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ksl_life;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Atif
 */
public class Problem1 {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String str3 = "1927-11-30 1:55:07";  
        String str4 = "1927-12-31 23:54:08";  
        Date sDt3 = sf.parse(str3);  
        Date sDt4 = sf.parse(str4);  
        long ld3 = sDt3.getTime();
        long ld4 = sDt4.getTime();
        System.out.println(ld3);
        System.out.println(ld4);
        System.out.println(ld4-ld3);
    }
}
