package com.polytech.jc.card;

public class CardError {
    /**
     * Converts a JavaCard status word to its description
     * @param sw Status word from the card
     * @return String describing the error or success status
     */
    public static String getDescription(short sw) {
        switch (sw) {
            case (short)0x9000:
                return "No Error";
            case (short)0x6100:
                return "Response bytes remaining";
            case (short)0x6700:
                return "Wrong length";
            case (short)0x6982:
                return "Security condition not satisfied";
            case (short)0x6983:
                return "File invalid";
            case (short)0x6984:
                return "Data invalid";
            case (short)0x6985:
                return "Conditions of use not satisfied";
            case (short)0x6986:
                return "Command not allowed (no current EF)";
            case (short)0x6999:
                return "Applet selection failed";
            case (short)0x6A80:
                return "Wrong data";
            case (short)0x6A81:
                return "Function not supported";
            case (short)0x6A82:
                return "File not found";
            case (short)0x6A83:
                return "Record not found";
            case (short)0x6A84:
                return "Not enough memory space in the file";
            case (short)0x6A86:
                return "Incorrect parameters (P1,P2)";
            case (short)0x6B00:
                return "Wrong P1P2";
            case (short)0x6C00:
                return "Correct Expected Length (Le)";
            case (short)0x6D00:
                return "INS value not supported";
            case (short)0x6E00:
                return "CLA value not supported";
            case (short)0x6F00:
                return "No precise diagnosis";
            default:
                return "Unknown status word: 0x" + Integer.toHexString(sw & 0xFFFF).toUpperCase();
        }
    }
}
