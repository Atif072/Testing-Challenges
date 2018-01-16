/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa_encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 *
 * @author Atif
 */
public class Bob {
    
    private byte[] encryptMsgByte,decryptMsgByte;
    private String plainMsg,text;
    PublicKey pubKeyB, pubKeyA;
    PrivateKey privateKeyB;
    SecretKey desKey;
    private String UNICODE_FORMAT = "UTF8";
    private DESedeKeySpec ks;
    private SecretKeyFactory skf;
    private SecretKey key;
    
    
    
   
   public Bob() throws FileNotFoundException, UnsupportedEncodingException {
      //PrintWriter writer = new PrintWriter("SharedData.txt", "UTF-8"); 
   }
   
   
   /*public void getPKA(Scanner input) {
        int counter = 0;
        String temp = "";
        while(input.hasNextLine() && counter < 3) {
            counter++;
        }
        while(input.hasNextLine() && counter<6) {
            temp+=input.nextLine();
            counter++;  
        }
        System.out.println();
        System.out.println(temp);
        System.out.println();
   }*/
   public void exchangePublicKey(PublicKey key) {
        this.pubKeyA = key;
    }
   public PublicKey getMyPublicKey() {
       return pubKeyB;
   }
   public void setDesKey(String desKey) {
        System.out.println("Bob sends DES Key: "+desKey);
        this.plainMsg = desKey;
    }
   public void setAutoDESKey(SecretKey k) {
        this.desKey = k;
    }
   public void getEncryptedDES(byte[] msg) throws Exception {
       
       System.out.println("Bob gets encrypted DES from Alice: "+msg);
       this.encryptMsgByte = msg;
       decryptMsgByte = decrypt(pubKeyA,encryptMsgByte);
       System.out.println("Bob gets DES from Alice: "+new String(decryptMsgByte));
       
   }
   
   public byte[] giveEncryptedDES() throws Exception {
       
       encryptMsgByte = encrypt(privateKeyB,plainMsg);
       System.out.println("Bob is giving Encrypted DES to Alice: "+encryptMsgByte.toString());
       return encryptMsgByte;
   }
   
   
    public void generateRSAKey() throws NoSuchAlgorithmException, FileNotFoundException {
       
        
        KeyPair keyPair = buildKeyPair();
        pubKeyB = keyPair.getPublic();
        privateKeyB = keyPair.getPrivate();
        
        
            
     }    
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);      
        return keyPairGenerator.genKeyPair();
    }
    
    private byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        System.out.println(privateKey+"   "+message);
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        
        return cipher.doFinal(message.getBytes());  
    }
    
    private byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        
        return cipher.doFinal(encrypted);
    }
    
    public byte[] sendText() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Scanner in = new Scanner(System.in);
        text = in.nextLine(); 
        
        System.out.println("Bob Enters text for sending to Alice: "+text);
        System.out.println();
        Cipher desCipher;
        desCipher = Cipher.getInstance("DESede");
        byte[] textByte = text.getBytes();
        
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] textEncrypted = desCipher.doFinal(textByte);
        
        System.out.println("Bob sends encrypted text to Alice: "+new String(textEncrypted));
        System.out.println();
        return textEncrypted;
    }
    
     public byte[] receiveText(byte[] s) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        
         System.out.println("Bob receives encryped text from Alice: "+new String(s));
         System.out.println();
         Cipher desCipher;
        // Create the cipher
        desCipher = Cipher.getInstance("DESede");
        desCipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] textEncrypted = desCipher.doFinal(s);
        System.out.println("Bob receive text from Alice: "+new String(textEncrypted));
        System.out.println();
        return textEncrypted;
        
    }
    public void generateDESKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("DESede");
        desKey = keygenerator.generateKey();
        
    }
    
    public SecretKey generateSecretKey(String plainKey) {
        byte[] arrayBytes;
        try {
            arrayBytes = plainKey.getBytes(UNICODE_FORMAT);
            ks = new DESedeKeySpec(arrayBytes);
            skf = SecretKeyFactory.getInstance("DESede");
            key = skf.generateSecret(ks);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("Genrate DES key : "+key.toString());
        //String encodedKey = Base64.getEncoder().encodeToString(desKey.getEncoded());
        //System.out.println("Genrate DES key : "+encodedKey);
        return key;
    }
    public void saveKey(File pubFile, File pvtFile, File shared) {
         try {
            ObjectOutputStream publicKeyBob = new ObjectOutputStream(new FileOutputStream(pubFile));
            ObjectOutputStream privateKeyBob = new ObjectOutputStream(new FileOutputStream(pvtFile));
            ObjectOutputStream publicSharedKeyBob = new ObjectOutputStream(new FileOutputStream(shared));
            publicKeyBob.writeObject(pubKeyB);
            publicKeyBob.writeObject(privateKeyB);
            publicSharedKeyBob.writeObject(pubKeyB);
            publicKeyBob.close();
            privateKeyBob.close();
            publicSharedKeyBob.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void readKey(File pubFile) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pubFile));
             final PublicKey publicKeyAlice = (PublicKey) inputStream.readObject();
             this.pubKeyA = publicKeyAlice;
             inputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void saveText(File sharedFile, byte[] text) {
         try {
            ObjectOutputStream textWriter = new ObjectOutputStream(new FileOutputStream(sharedFile));
            textWriter.writeObject(text);
            textWriter.close();
         } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public byte[] fetchText(File sharedF) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sharedF));
        final byte[] text = (byte[]) inputStream.readObject();
        inputStream.close();
        return text;
    }
    
    
 }
