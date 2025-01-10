package com.polytech.jc.card;

public enum CardInstruction {
    INS_PUSH_HEADER_PACKET((byte)0x01),
    INS_PUSH_DATA_PACKET((byte)0x02),
    INS_READ_DATA_PACKET((byte)0x03),
    INS_EXEC_PACKET((byte)0x04),
    INS_READ_BUFFER((byte)0x05),
    INS_READ_PACKET_HEADER((byte)0x06);

    final byte instruction;

    CardInstruction(byte instruction){
        this.instruction = instruction;
    }
}
