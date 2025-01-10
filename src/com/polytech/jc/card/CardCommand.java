package com.polytech.jc.card;

public enum CardCommand {
    CMD_ENCRYPT_CART((byte)0x08),
    CMD_CHECK_PIN((byte)0x9),
    CMD_GET_PUBLIC_EXP((byte)0xA),
    CMD_GET_PUBLIC_MOD((byte)0xB),
    CMD_GET_DOMAIN_NAME((byte)0xC);

    final byte command;

    CardCommand(byte value){
        command = value;
    }

    public byte getCommand(){
        return command;
    }
}
