package com.polytech.jc.card;

import com.polytech.jc.Utils;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import java.util.Arrays;

import static com.polytech.jc.Utils.concatNibbles;

public class CardDialog {
    private CardChannel cardChannel;
    private byte CLA;

    public CardDialog(CardChannel cardChannel, byte CLA){
        this.cardChannel = cardChannel;
        this.CLA = CLA;
    }

    public void select(byte[] aid) throws CardException {
        ResponseAPDU selectResponse = this.cardChannel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
        int sw = selectResponse.getSW();
        if (sw != 0x9000) {
            throw new CardException(CardError.getDescription((short)sw));
        }
        System.out.println("Applet selected successfully.");
    }

    public byte[] transact(CardCommand command, byte[] data) throws CardException {
        byte numberPacket = (byte)((data.length / 255)+1);
        byte commandPacket = command.getCommand();

        System.out.println(((data.length / 255)+1));
        System.out.println(Utils.toBinaryString(numberPacket));
        System.out.println(Utils.toBinaryString(commandPacket));

        byte[] header = new byte[1];
        header[0] = concatNibbles(commandPacket, numberPacket);

        System.out.println(Utils.toBinaryString(header[0]));

        ResponseAPDU r = this.cardChannel.transmit(new CommandAPDU(this.CLA, CardInstruction.INS_PUSH_HEADER_PACKET.instruction, 0x00, 0x00, header));
        int sw = r.getSW();
        if (sw != 0x9000) {
            throw new CardException(CardError.getDescription((short)sw));
        }

        byte[][] packets_data = Utils.split(data, (short)0, (short)data.length);
        for (int i=0;i<packets_data.length;i++) {
            byte[] packetsDatum = packets_data[i];
            System.out.println("Send sub packet " + (i+1) + "/" + packets_data.length);
            ResponseAPDU response = this.cardChannel.transmit(new CommandAPDU(this.CLA,
                    CardInstruction.INS_PUSH_DATA_PACKET.instruction,
                    0x00,
                    0x00,
                    packetsDatum));
            sw = response.getSW();
            if (sw != 0x9000) {
                throw new CardException(CardError.getDescription((short) sw));
            }
        }

        r = this.cardChannel.transmit(new CommandAPDU(this.CLA, CardInstruction.INS_EXEC_PACKET.instruction, 0x00, 0x00, 127));
        sw = r.getSW();
        if (sw != 0x9000) {
            throw new CardException(CardError.getDescription((short)sw));
        }

        byte result = r.getData()[0];
        byte[][] packets_result = new byte[result][];
        short len = 0;
        for(int i=0;i<packets_result.length;i++){
            r = this.cardChannel.transmit(new CommandAPDU(this.CLA, CardInstruction.INS_READ_DATA_PACKET.instruction, 0x00, 0x00, 255));
            sw = r.getSW();
            if (sw != 0x9000) {
                throw new CardException(CardError.getDescription((short)sw));
            }
            packets_result[i] = new byte[r.getNr()];
            System.arraycopy(r.getData(), 0, packets_result[i], 0, r.getNr());
            len += (short)r.getNr();
        }
        return Utils.merge(packets_result, len);
    }

    public byte[] readPacketHeader() throws CardException {
        ResponseAPDU r = this.cardChannel.transmit(new CommandAPDU(this.CLA, CardInstruction.INS_READ_PACKET_HEADER.instruction, 0x00, 0x00, 100));
        int sw = r.getSW();
        if (sw != 0x9000) {
            throw new CardException(CardError.getDescription((short)sw));
        }
        return r.getData();
    }
}
