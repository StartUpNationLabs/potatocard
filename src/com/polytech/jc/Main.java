package com.polytech.jc;

import com.polytech.jc.card.CardCommand;
import com.polytech.jc.card.CardDialog;
import com.polytech.jc.card.CardError;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.smartcardio.*;

public class Main {
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

        byte[] data = new byte[300];
        Arrays.fill(data, (byte) 2);
        byte[] result = dialog.transact(CardCommand.CMD_ENCRYPT_CART, data);

        System.out.println(Utils.toBinaryString(dialog.readPacketHeader()[0]));
        System.out.println("Output size:" + result.length);

        card.disconnect(false);
    }
}
