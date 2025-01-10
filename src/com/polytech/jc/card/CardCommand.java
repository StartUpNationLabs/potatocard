package com.polytech.jc.card;

public enum CardCommand {
    CMD_ENCRYPT_CART((byte)0x08);

    final byte command;

    CardCommand(byte value){
        command = value;
    }

    public byte getCommand(){
        return command;
    }
}
