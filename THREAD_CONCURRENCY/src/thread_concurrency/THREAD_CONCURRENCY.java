/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thread_concurrency;

/**
 *
 * @author Atif
 */
public class THREAD_CONCURRENCY {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        String importantInfo[] = {
            "Mares eat oats",
            "Does eat oats",
            "Little lambs eat ivy",
            "A kid will eat ivy too"
        };

        for (int i = 0;
             i < importantInfo.length;
             i++) {
            //Pause for 4 seconds
            Thread.sleep(10000);
            //Print a message
            System.out.println(importantInfo[i]);
        }
    }
}
