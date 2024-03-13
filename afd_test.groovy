import java.io.*;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.security.MessageDigest;
import groovy.util.XmlSlurper;
import java.nio.charset.Charset;

def xmlSlurper = new XmlSlurper();
// def object = xmlSlurper.parseText(job.getDocuments()[0].getText());
// def xmlfile = new File("75100261164255_20231221.xml");
def xmlfile = new File("add_new_element_busType2_encrypted.xml");
// println("xmlfile: " + xmlfile.getText());
def object = xmlSlurper.parseText(xmlfile.getText());
// println("object: " + object);

def iv_string = object.iv;
println("iv string is: " + iv_string);
      
def lable = object.label;
println("label is: " + lable);

def encryptedxmlbody = object.body;
println("encryptedxmlbody is: " + encryptedxmlbody);

File passwordFile = new File("hkt-encrypt-pwd.cfg"); 
      
def keyPassword = "";
if(passwordFile.exists()){
    keyPassword = passwordFile.readLines().get(0);
    println("File exist and the key password is " + keyPassword);
}else{
    println("File not exist and the key password is " + keyPassword);
}

String decryptedxml = decryptString("mwLxtz2rM0DFQnQYBOlY0DXdKz/yyMC+syNXxrPpYmk=", "W3GOHYcmuwPSlg713erzJw==", encryptedxmlbody.text());
println("decryptedxml: " + decryptedxml);

print("processing finished");

public static String encryptString(String key, String ivstr, String inputStr) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivstr));
        Key encryptionKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");

        byte[] content = null;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, iv);
        content = cipher.doFinal(inputStr.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(content);
        return encryptedString;

}
public static String decryptString(String key, String ivstr, String inputStr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivstr.getBytes(StandardCharsets.UTF_8)));//iv
    Key encryptionKey = new SecretKeySpec(Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8)), "AES");//key

    byte[] content = Base64.getDecoder().decode(inputStr.getBytes(StandardCharsets.UTF_8));
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, encryptionKey, iv);
    content = cipher.doFinal(content);

    String decryptedString = new String(content, "UTF-8");
    
    return decryptedString;
}




