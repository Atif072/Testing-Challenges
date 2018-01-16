/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecteuler;

/**
 *
 * @author Atif
 */
public class Problem_4 {
    
    public static int make_reverse(int n) {
        int x = 0, base = 100000;
        while(n>0) {
            x += (n%10)*base;
            base = base/10;
            n = n/10;
            //System.out.println(n+" "+x);
        }
        return x;
    } 
    
    
}
