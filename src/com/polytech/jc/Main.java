package com.polytech.jc;

import com.polytech.jc.card.CardCommand;
import com.polytech.jc.card.CardDialog;
import com.polytech.jc.card.CardError;
import com.polytech.jc.card.CardInstruction;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.smartcardio.*;

public class Main {
    private static final byte[] SERVER_KEY_N = {
            (byte) 0x8B,(byte) 0x8B,(byte) 0x64,(byte) 0xBC,(byte) 0x28,(byte) 0xE9,(byte) 0x9D,(byte) 0x2C,(byte) 0x20,(byte) 0x3A,(byte) 0x39,(byte) 0xA3,(byte) 0xAD,(byte) 0x4E,(byte) 0x59,(byte) 0x29,(byte) 0x6B,(byte) 0xC1,(byte) 0x24,(byte) 0x33,(byte) 0xE9,(byte) 0x58,(byte) 0xD4,(byte) 0x4A,(byte) 0x6A,(byte) 0x0E,(byte) 0x1C,(byte) 0x9D,(byte) 0x8E,(byte) 0x60,(byte) 0xBD,(byte) 0x09,(byte) 0xED,(byte) 0xDE,(byte) 0x53,(byte) 0x11,(byte) 0xF0,(byte) 0xD0,(byte) 0xDA,(byte) 0x09,(byte) 0xC9,(byte) 0x7C,(byte) 0x3E,(byte) 0x31,(byte) 0xD9,(byte) 0xB5,(byte) 0x3D,(byte) 0x62,(byte) 0x1B,(byte) 0xE6,(byte) 0x0A,(byte) 0xE7,(byte) 0x1E,(byte) 0xB8,(byte) 0x2B,(byte) 0x40,(byte) 0xB3,(byte) 0xF4,(byte) 0xA1,(byte) 0x4A,(byte) 0x82,(byte) 0x2B,(byte) 0x48,(byte) 0x57,(byte) 0x42,(byte) 0xF8,(byte) 0x46,(byte) 0x26,(byte) 0x5B,(byte) 0xE4,(byte) 0xA6,(byte) 0xE7,(byte) 0x05,(byte) 0x33,(byte) 0x77,(byte) 0xD7,(byte) 0x27,(byte) 0xAC,(byte) 0x92,(byte) 0x0E,(byte) 0x03,(byte) 0xB0,(byte) 0xFE,(byte) 0xDE,(byte) 0x60,(byte) 0x04,(byte) 0x6D,(byte) 0x8D,(byte) 0x35,(byte) 0xE6,(byte) 0xEA,(byte) 0x33,(byte) 0xDA,(byte) 0x25,(byte) 0x89,(byte) 0x3D,(byte) 0x37,(byte) 0xE2,(byte) 0x93,(byte) 0x84,(byte) 0xFC,(byte) 0xB2,(byte) 0x85,(byte) 0x53,(byte) 0x0F,(byte) 0x3E,(byte) 0x14,(byte) 0x66,(byte) 0xAB,(byte) 0xD1,(byte) 0xE7,(byte) 0x5A,(byte) 0xD1,(byte) 0x25,(byte) 0xA5,(byte) 0xF3,(byte) 0x5D,(byte) 0x98,(byte) 0x93,(byte) 0xA4,(byte) 0x22,(byte) 0x45,(byte) 0x81,(byte) 0x02,(byte) 0x6E,(byte) 0xFB,(byte) 0xB4,(byte) 0x7F,(byte) 0xBF,(byte) 0x4A,(byte) 0x5E,(byte) 0x81,(byte) 0xE9,(byte) 0x4E,(byte) 0xD6,(byte) 0x18,(byte) 0xC4,(byte) 0x1A,(byte) 0xC1,(byte) 0x6A,(byte) 0xA4,(byte) 0x74,(byte) 0xF5,(byte) 0x6E,(byte) 0x08,(byte) 0xC5,(byte) 0xB5,(byte) 0x71,(byte) 0x79,(byte) 0x61,(byte) 0x1B,(byte) 0x17,(byte) 0xE4,(byte) 0x64,(byte) 0x96,(byte) 0x65,(byte) 0xE2,(byte) 0x8C,(byte) 0x03,(byte) 0x67,(byte) 0xA9,(byte) 0x14,(byte) 0x75,(byte) 0x1D,(byte) 0xB5,(byte) 0xD6,(byte) 0x7E,(byte) 0xE5,(byte) 0xCE,(byte) 0xA7,(byte) 0xAD,(byte) 0xBB,(byte) 0xF9,(byte) 0xF8,(byte) 0xBF,(byte) 0x69,(byte) 0x8F,(byte) 0x66,(byte) 0xD4,(byte) 0x43,(byte) 0xC2,(byte) 0x79,(byte) 0xCD,(byte) 0x6F,(byte) 0xCF,(byte) 0x3D,(byte) 0x9A,(byte) 0x36,(byte) 0x9E,(byte) 0x83,(byte) 0x7E,(byte) 0xA8,(byte) 0xFD,(byte) 0x05,(byte) 0x7C,(byte) 0x79,(byte) 0x63,(byte) 0xC3,(byte) 0x54,(byte) 0x16,(byte) 0x9B,(byte) 0x89,(byte) 0x2C,(byte) 0x95,(byte) 0x8A,(byte) 0xE8,(byte) 0xBB,(byte) 0x11,(byte) 0xBA,(byte) 0xD5,(byte) 0x1E,(byte) 0x75,(byte) 0xD9,(byte) 0xE0,(byte) 0x9F,(byte) 0x53,(byte) 0x75,(byte) 0x02,(byte) 0xDB,(byte) 0xF8,(byte) 0xD4,(byte) 0xD1,(byte) 0x9B,(byte) 0xEE,(byte) 0xF6,(byte) 0x36,(byte) 0x19,(byte) 0x86,(byte) 0x62,(byte) 0x25,(byte) 0x69,(byte) 0x9F,(byte) 0xA2,(byte) 0xDA,(byte) 0x10,(byte) 0xD9,(byte) 0x33,(byte) 0xE1,(byte) 0xB6,(byte) 0xF8,(byte) 0xE7,(byte) 0xA2,(byte) 0x9F,(byte) 0x72,(byte) 0x4A,(byte) 0xBE,(byte) 0x87,(byte) 0x15,(byte) 0x4F,(byte) 0x03,(byte) 0xDF,(byte) 0x0C,(byte) 0xC4,(byte) 0xFA,(byte) 0x53,(byte) 0xAF
    };

    public static void decrypt(byte[] a) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // La clé privée en format PEM (Base64) que vous avez fourni
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIExAIBADANBgkqhkiG9w0BAQEFAASCBK4wggSqAgEAAoIBAQCLi2S8KOmdLCA6\n" +
                "OaOtTlkpa8EkM+lY1EpqDhydjmC9Ce3eUxHw0NoJyXw+Mdm1PWIb5grnHrgrQLP0\n" +
                "oUqCK0hXQvhGJlvkpucFM3fXJ6ySDgOw/t5gBG2NNebqM9oliT034pOE/LKFUw8+\n" +
                "FGar0eda0SWl812Yk6QiRYECbvu0f79KXoHpTtYYxBrBaqR09W4IxbVxeWEbF+Rk\n" +
                "lmXijANnqRR1HbXWfuXOp627+fi/aY9m1EPCec1vzz2aNp6Dfqj9BXx5Y8NUFpuJ\n" +
                "LJWK6LsRutUeddngn1N1Atv41NGb7vY2GYZiJWmfotoQ2TPhtvjnop9ySr6HFU8D\n" +
                "3wzE+lOvAgMBAAECggEANs6MxgoblwuZrMSusIaxJj+YICvzXlwizfy06HzfKeAY\n" +
                "eNWPmlGtEWL9VUAnDbQKMILtjVDXbMWA6Hfg17dANmWrNYIjXW7gFX7ddreyY7r1\n" +
                "YDL6DCQxEuE0zI+JAm0gii6HWhgV1Z+FwRAqMxnCED1M2Bz5ic+SzMB61K+Kfp+K\n" +
                "sjiWC0ehK0TvPVnpgiIVbKwz5Jb+uB6zE0LZ4p9hf48xrtZSsv9BlatrF/7fRtXf\n" +
                "aAFXK0e8T9CLFP1zS4QAZcElBoZ1RJiVIiLJTnk9UpRpEZdZH2Pv8c/sXEkuIPNn\n" +
                "BAnxTz9AczSWqSnyZk026a+3PIZJSNXr5kqNgLAsAQKBiQCf845nk6GJWsa2WImX\n" +
                "OFrRiY8kX9pWIJLj3NhXKI8IUd36etwgxYd+Xi1hr2WxLZprPZctkK63NRHX5+nA\n" +
                "YWY3YaNTTpnnkRkJdYcPPQPxZ43ugud/v+psedK5N5xmIqTtOIKm067LSlYtKg82\n" +
                "RXck8l2uv3B3TsBpkiKx3V8yPUplkTWaqheBAnkA31bMpng1O6iDdNAL2JCuB8Dv\n" +
                "Ud6ZJRXhP+kLeW/W2N4EZ2pDrijqhBKk6l7lLzPdotevkZFjuO5WcjapQF6ardes\n" +
                "Aj25qOBw4g1bA9ACfh7bz+/GgwqbKQV8VbFmquuSjGym1rHKIKPWHdhUFaJVcs3g\n" +
                "KM4aOYMvAoGJAIGoiAY0KRzTpgMIUDhx6USz0oH05WMtsoTdb81vRkrDOkKiNzCl\n" +
                "jokERJtL+A4g3m0SFytLfiVq8pTnHJfjHF3GWwPb0mxhNT9ystHKbxjQDcCLAqeO\n" +
                "5bObKRMVHkW4zaKyTU54h9eo4aRo1L30n3kmsoRGER/VianKWS9/OHzcLCObCy3d\n" +
                "tYECeQDKZr5m6y/LFie0OEtUA6JEEpyQhHwjuNV+9vNYJXv+5A33rr0RW7FU6hSc\n" +
                "9k0bXxSwXGmXMby/PvgG3/S5CkOB4p/XFBtqqaSDqHjgVqW7qLmqk3FDFtyQ65DH\n" +
                "lFMn+P/B1UHJ9ivpMrF8MK3bQVfRCDTqpgXsMy8CgYh8X0550LBtbDVeSdfUB6zA\n" +
                "aeB5/EqKyL+jvh9SYNn2o8OlG3dO9a/YqHHVstKWmZ5Naen3EVLBeD++owiO0jjG\n" +
                "qb4epKzUyy//NXVN0ZOHgR5hLJBxrsJRL2wfTZW1jfhQ0lQEbCCg1LAMWG1DcKED\n" +
                "Gq+a+ptZjcVz+s1bxjGbxTogUMRTWc9+\n" +
                "-----END PRIVATE KEY-----\n";

        // Suppression des lignes de la clé privée
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replace("\n", "");

        // Décodage de la clé privée en Base64
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        // Génération de la clé privée RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

        // Message chiffré à déchiffrer (exemple sous forme de byte array)
        byte[] encryptedMessage = a;

        // Déchiffrement du message
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        System.out.println(Utils.toBinaryString(decryptedMessage[0]) + "/" + Utils.toBinaryString(decryptedMessage[1]) + "/" + Utils.toBinaryString(decryptedMessage[2]) + "/" + Utils.toBinaryString(decryptedMessage[3]));
        // Affichage du message déchiffré

    }

    public static void main(String [] args) throws CardException, Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        System.out.println("Terminals : " + terminals);

        CardTerminal terminal = terminals.get(0);
        // establish a connection with the card
        Card card = terminal.connect("T=0");
        System.out.println("card: " + card);
        CardChannel channel = card.getBasicChannel();

        // AID de votre applet
        byte[] aid = { (byte) 0xA0, 0x00, 0x00, 0x00, 0x62, 0x03, 0x01, 0x0C, 0x06, 0x01, 0x02 };
        CardDialog dialog = new CardDialog(channel, (byte)0x80);

        dialog.select(aid);

        byte[] data = ByteBuffer.allocate(4).putInt(1234).array();
        System.out.println(Utils.toBinaryString(data[0]) + "/" + Utils.toBinaryString(data[1]) + "/" + Utils.toBinaryString(data[2]) + "/" + Utils.toBinaryString(data[3]));
        byte[] pinData = {data[2], data[3]};
        System.out.println(Utils.toBinaryString(pinData[0]) + "/" + Utils.toBinaryString(pinData[1]));
        byte[] result = dialog.transact(CardCommand.CMD_GET_PUBLIC_EXP, pinData);
        byte[] result2 = dialog.transact(CardCommand.CMD_GET_PUBLIC_MOD, pinData);

        //System.out.println(Utils.toBinaryString());
        System.out.println("Output size:" + result.length);
        System.out.println("Output size:" + result2.length);

        // Conversion en BigInteger
        BigInteger modulus = new BigInteger(1, result2); // Hexadécimal
        BigInteger exponent = new BigInteger(1, result);

        // Créer la spécification de la clé publique
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);

        // Utiliser KeyFactory pour générer une clé publique RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Afficher la clé publique
        System.out.println("Clé publique RSA : " + publicKey);

        byte[] result3 = dialog.transact(CardCommand.CMD_ENCRYPT_CART, data);

        Main.decrypt(result3);

        byte[] result4 = dialog.transact(CardCommand.CMD_GET_DOMAIN_NAME, data);

        System.out.println(new String(result4));


        card.disconnect(false);
    }
}
