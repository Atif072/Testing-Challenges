/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeadLock;

/**
 *
 * @author Atif
 */
class Thread2 extends Thread
{
    private Shared s1;
    private Shared s2;
 
    // constructor to initialize fields
    public Thread2(Shared s1, Shared s2)
    {
        this.s1 = s1;
        this.s2 = s2;
    }
 
    // run method to start a thread
    @Override
    public void run()
    {
        // taking object lock of s2
        // enters into test2 method
        s2.test2(s1);
    }
}
