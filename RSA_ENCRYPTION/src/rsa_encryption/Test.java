/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa_encryption;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 *
 * @author Atif
 */
public class Test {
     
    public static void main(String[] args) throws NoSuchAlgorithmException, Exception {
        
       
        File bobPubF = new File("src/Bob/public.key");
        File bobPvtF = new File("src/Bob/private.key");
        File alicePvtF = new File("src/Alice/private.key");
        File alicePubF = new File("src/Alice/public.key");
        //------------public key each other-----------//
        File aliceSharedPubF = new File("src/Shared/Alice.key");
        File bobSharedPubF = new File("src/Shared/Bob.key");
        //-----------Text share Room---------------//
        File textRoomF = new File("src/Shared/TextRoom.txt");
        
        Bob b = new Bob();
        Alic a = new Alic();
        
        b.generateRSAKey();
        a.generateRSAKey();
        
        b.saveKey(bobPubF,bobPvtF,bobSharedPubF);
        a.saveKey(alicePubF,alicePvtF,aliceSharedPubF);
      
        
        b.readKey(aliceSharedPubF);
        a.readKey(bobSharedPubF);
       
        
        String desKey = "404142434445464748494A4B";
        a.setDesKey(desKey);
        b.getEncryptedDES(a.giveEncryptedDES());
        
        
        a.setAutoDESKey(b.generateSecretKey(desKey));
        b.setAutoDESKey(a.desKey);
        String c = "Yes";
        String who = "";
        Scanner in = new Scanner(System.in);
        
        while (c.equals("Yes")) {
            System.out.println();
            System.out.println("Enter 'No' if exit Or 'Yes' for Continue.....");
            System.out.println();
            c = in.nextLine(); 
            if(c.equals("No")) {
                break;
            }
            System.out.println();
            System.out.println("Alice or Bob : ");
            System.out.println();
            who = in.nextLine(); 
            
            if(who.equals("Bob")) {
                byte[] text = b.sendText();
                b.saveText(textRoomF, text);
                a.receiveText(a.fetchText(textRoomF));
            } 
            if(who.equals("Alice")) {
                byte[] text = a.sendText();
                a.saveText(textRoomF, text);
                b.receiveText(a.fetchText(textRoomF));
            }
         }
    }  
}
