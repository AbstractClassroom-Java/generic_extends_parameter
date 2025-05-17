package io.github.nathanjrussell.binary_code;

public class Codeword {
    int[] bitIntArray;
    private final int codewordLength;

    Codeword(int length) {
        int numInts = (length + 31) / 32;
        this.codewordLength = length;
        bitIntArray = new int[numInts];
        for (int i = 0; i < numInts; i++) {
            bitIntArray[i] = 0;
        }
    }

    Codeword(int codewordLength, int[] bitIntArray) {
        this.codewordLength = codewordLength;
        this.bitIntArray = bitIntArray;
    }

    public boolean getBit(int index) {
        int intIndex = index / 32;
        int bitIndex = index % 32;
        return (bitIntArray[intIndex] & (1 << bitIndex)) != 0;
    }

    public void setBit(int index) {
        int intIndex = index / 32;
        int bitIndex = index % 32;
        bitIntArray[intIndex] |= (1 << bitIndex);
    }

    public void clearBit(int index) {
        int intIndex = index / 32;
        int bitIndex = index % 32;
        bitIntArray[intIndex] &= ~(1 << bitIndex);
    }

    public void setBit(int index, int value) {
        int intIndex = index / 32;
        int bitIndex = index % 32;
        if (value == 1) {
            bitIntArray[intIndex] |= (1 << bitIndex);
        } else {
            bitIntArray[intIndex] &= ~(1 << bitIndex);
        }
    }

    public int getLength() {
        return this.codewordLength;
    }

    public int[] getBitIntArray() {
        return bitIntArray;
    }

    public Codeword copy() {
        int[] newBitIntArray = new int[bitIntArray.length];
        System.arraycopy(bitIntArray, 0, newBitIntArray, 0, bitIntArray.length);
        return new Codeword(this.codewordLength, newBitIntArray);
    }

    public Codeword add(Codeword other) {
        int[] newBitIntArray = new int[bitIntArray.length];
        for (int i = 0; i < bitIntArray.length; i++) {
            newBitIntArray[i] = bitIntArray[i] ^ other.bitIntArray[i];
        }
        return new Codeword(this.codewordLength, newBitIntArray);
    }

    public void addInPlace(Codeword other) {
        for (int i = 0; i < bitIntArray.length; i++) {
            bitIntArray[i] ^= other.bitIntArray[i];
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int coordinate = 0; coordinate < this.codewordLength; coordinate++) {
            sb.append(this.getBit(coordinate) ? "1" : "0");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Codeword codeword = new Codeword(5);
        codeword.setBit(0, 1);
        codeword.setBit(1, 0);
        codeword.setBit(2, 0);
        codeword.setBit(3, 1);
        codeword.setBit(4, 0);
        System.out.println(codeword);// Output: 10101
    }
}
