package com.safexpay.android;

import com.safexpay.android.Utils.CryptoUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkEncryption(){
        String Key="cBR6TCY0FovgdCyJo5ZMRcfAyxuuy9h8YU20fePdK/o=";
        String txnreq="Data toencrypt";

        String Encryptedrequest1= CryptoUtils.encrypt(txnreq, Key);


        System.out.println("Encryptedrequest1 : "+Encryptedrequest1);


        String DecrptedResponse=CryptoUtils.decrypt(Encryptedrequest1, Key);

        assertEquals(txnreq, DecrptedResponse);
    }
}