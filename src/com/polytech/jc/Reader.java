package com.polytech.jc;


import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.smartcardio.*;

public class Reader {
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

        // Envoyer la commande SELECT pour sélectionner l'applet
        ResponseAPDU selectResponse = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));

        // Vérification du statut de sélection
        if (selectResponse.getSW() == 0x9000) {
            System.out.println("Applet selected successfully.");

            byte[] data = new byte[10];
            data[0] = 0x48;
            data[1] = 0x65;
            data[2] = 0x6c;

            ResponseAPDU r = channel.transmit(new CommandAPDU(0x80, 0x02, 0x00, 0x00, 127));
            int sw = r.getSW();
            if (sw == 0x9000) {
                byte[] responseData = r.getData();
                System.out.println("Response: OK");
                if(data[0] == responseData[0] && data[1] == responseData[1] && data[2] == responseData[2]) {
                    System.out.println("Data read successfully");
                } else {
                    System.out.println("Data read failed");
                }
            } else {
                System.out.printf("Failed with status: %04X\n", sw);
            }
        } else {
            System.out.println("Failed to select applet, status: " + selectResponse.getSW());
        }

        card.disconnect(false);
    }
}
