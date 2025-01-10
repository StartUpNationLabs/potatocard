package com.polytech.jc;

public class Utils {
    public static byte concatNibbles(byte firstNibble, byte secondNibble) {
        // Extract 4 least significant bits from first byte and shift them left
        byte high = (byte)((firstNibble & 0x0F) << 4);

        // Extract 4 least significant bits from second byte
        byte low = (byte)(secondNibble & 0x0F);

        // Combine both parts
        return (byte)(high | low);
    }

    public static String toBinaryString(byte b) {
        // Since we're on JavaCard, we'll avoid using String concatenation
        char[] result = {'0','0','0','0','0','0','0','0'};

        for (byte i = 0; i < 8; i++) {
            // Check if bit is set using mask and shift
            if ((b & (1 << (7 - i))) != 0) {
                result[i] = '1';
            }
        }

        return new String(result);
    }

    public static byte[][] split(byte[] data, short offset, short length) {
        // Calculate number of required arrays (rounded up)
        short numArrays = (short)((length + 254) / 255);
        byte[][] result = new byte[numArrays][];

        short remaining = length;
        short currentOffset = offset;

        for(short i = 0; i < numArrays; i++) {
            // For last array, might need smaller size
            int currentLength = (remaining > 255 ? 255 : remaining);
            result[i] = new byte[currentLength];

            // Copy data to new array
            for(short j = 0; j < currentLength; j++) {
                result[i][j] = data[(short)(currentOffset + j)];
            }

            currentOffset += 255;
            remaining -= 255;
        }

        return result;
    }

    public static byte[] merge(byte[][] arrays, short totalLength) {
        byte[] result = new byte[totalLength];
        short currentOffset = 0;

        for(short i = 0; i < arrays.length; i++) {
            for(short j = 0; j < arrays[i].length; j++) {
                result[currentOffset++] = arrays[i][j];
            }
        }

        return result;
    }
}
