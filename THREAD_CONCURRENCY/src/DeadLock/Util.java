/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeadLock;

/**
 *
 * @author Atif
 */
// Java program to illustrate Deadlock
// in multithreading.
class Util
{
    // Util class to sleep a thread
    static void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
 