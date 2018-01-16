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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
import static rsa_encryption.Bob.buildKeyPair;


/**
 *
 * @author Atif
 */
public class Alic{
    
    private byte[] encryptMsgByte,decryptMsgByte;
    private String plainMsg,text;
    PublicKey pubKeyA, pubKeyB;
    PrivateKey privateKeyA; 
    SecretKey desKey;
    private String UNICODE_FORMAT = "UTF8";
    private DESedeKeySpec ks;
    private SecretKeyFactory skf;
    private SecretKey key;
    File file = new File("shared.txt");
    PrintWriter output = new PrintWriter(file);
    
    public Alic() throws FileNotFoundException, UnsupportedEncodingException {
      //PrintWriter writer = new PrintWriter("SharedData.txt", "UTF-8"); 
    }
            
    public void exchangePublicKey(PublicKey key) {
        this.pubKeyB = key;
    }
    public void setAutoDESKey(SecretKey k) {
        this.desKey = k;
        
    }
    public PublicKey getMyPublicKey() {
       return pubKeyA;
    }
    public void setDesKey(String desKey) {
        System.out.println("Alice sends DES Key: "+desKey);
        this.plainMsg = desKey;
     }
    /*public void getPKB(Scanner input) {
        
        int counter = 0;
        String temp = "";
        while(input.hasNextLine() && counter < 3)
        {
            temp+=input.nextLine();
            counter++;
        }
        
        System.out.println();
        System.out.println(temp);
        System.out.println();
   }*/
    public void getEncryptedDES(byte[] msg) throws Exception {
       
       System.out.println("Alice gets encrypted DES from Alice: "+msg);
       this.encryptMsgByte = msg;
       decryptMsgByte = decrypt(pubKeyB,encryptMsgByte);
       String temp = new String(decryptMsgByte);
       System.out.println("Alice gets DES from Bob: "+temp);
       
   }
   
   public byte[] giveEncryptedDES() throws Exception {
       
       encryptMsgByte = encrypt(privateKeyA,plainMsg);
       System.out.println("Alice is giving Encrypted DES to Alice: "+encryptMsgByte.toString());
       return encryptMsgByte;
   }
   
   
    public void generateRSAKey() throws NoSuchAlgorithmException {
       
        
        KeyPair keyPair = buildKeyPair();
        pubKeyA = keyPair.getPublic();
        privateKeyA = keyPair.getPrivate();
        
            
        
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
        System.out.println("Alice Enters text for sending to Bob: "+text);
        System.out.println();
        Cipher desCipher;
        desCipher = Cipher.getInstance("DESede");
        byte[] textByte = text.getBytes();
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] textEncrypted = desCipher.doFinal(textByte);
        System.out.println("Alice sends encrypted text to Bob: "+new String(textEncrypted));
        System.out.println();
        return textEncrypted;
    }
    
     public byte[] receiveText(byte[] s) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        System.out.println("Alice receives encryped text from Bob: "+new String(s));
        System.out.println();
        Cipher desCipher;
        desCipher = Cipher.getInstance("DESede");
        desCipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] textEncrypted = desCipher.doFinal(s);
        System.out.println("Alice receive text from Bob: "+new String(textEncrypted));
        System.out.println();
        return textEncrypted;
        
    }
     
    
    public void generateDESKey() throws NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
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
        System.out.println("Genrate DES key : "+key.toString());
        //String encodedKey = Base64.getEncoder().encodeToString(desKey.getEncoded());
        //System.out.println("Genrate DES key : "+encodedKey);
        return key;
    }
    
    public void saveKey(File pubFile, File pvtFile, File shared) {
         try {
            ObjectOutputStream publicKeyAlice = new ObjectOutputStream(new FileOutputStream(pubFile));
            ObjectOutputStream privateKeyAlice = new ObjectOutputStream(new FileOutputStream(pvtFile));
            ObjectOutputStream publicSharedKeyAlice = new ObjectOutputStream(new FileOutputStream(shared));
            publicKeyAlice.writeObject(pubKeyA);
            publicKeyAlice.writeObject(privateKeyA);
            publicSharedKeyAlice.writeObject(pubKeyA);
            
            publicKeyAlice.close();
            privateKeyAlice.close();
            publicSharedKeyAlice.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void readKey(File pubFile) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pubFile));
             final PublicKey publicKeyBob = (PublicKey) inputStream.readObject();
             this.pubKeyB = publicKeyBob;
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
