package io.github.nathanjrussell.binary_code;

import java.util.ArrayList;
import java.util.List;

public class GeneratorMatrix {
    private final int codewordLength;
    private final int codeDimension;
    Codeword[] minGenerators;

    private GeneratorMatrix(Codeword[] minGenerators) {
        this.minGenerators = minGenerators;
        this.codewordLength = minGenerators[0].getLength();
        this.codeDimension = minGenerators.length;
    }

    public int getLength() {
        return codewordLength;
    }

    public int getDimension() {
        return codeDimension;
    }

    public Codeword getGeneratorRow(int index) {
        return minGenerators[index];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Codeword generator : minGenerators) {
            sb.append(generator.toString()).append("\n");
        }
        return sb.toString();
    }

    // use a builder class to create the generator matrix
    public static class Builder {
        private final int codewordLength;
        private final List<Codeword> generators;


        public Builder(int codewordLength) {
            this.codewordLength = codewordLength;
            this.generators = new ArrayList<>();
        }

        public Builder addGeneratorRow(Codeword cw) {
            validateCodeword(cw);
            generators.add(cw);
            return this;
        }

        private void validateCodeword(Codeword cw) {
            if (cw.getLength() != codewordLength) {
                throw new IllegalArgumentException("Codeword length does not match generator matrix length");
            }
            boolean isAllZero = true;
            for (int i = 0; i < cw.getLength(); i++) {
                if (cw.getBit(i)) {
                    isAllZero = false;
                    break;
                }
            }
            if (isAllZero) {
                throw new IllegalArgumentException("Codeword cannot be all zeros");
            }
        }

        private boolean isAllZero(Integer[] array) {
            for (int i : array) {
                if (i != 0) {
                    return false;
                }
            }
            return true;
        }

        public GeneratorMatrix buildSystematic() {
            if (generators.isEmpty()) {
                throw new IllegalArgumentException("No generators added");
            }

            List<Integer[]> generatorList = new ArrayList<>();
            for (Codeword generator : generators) {
                Integer[] generatorArray = new Integer[generator.getLength()];
                for (int i = 0; i < generator.getLength(); i++) {
                    generatorArray[i] = generator.getBit(i) ? 1 : 0;
                }
                generatorList.add(generatorArray);
            }

            int currentRow = 0;
            while (currentRow < generatorList.size()) {
                if (generatorList.get(currentRow)[currentRow] == 0) {
                    boolean found = false;
                    for (int col = currentRow + 1; col < this.codewordLength; col++) {
                        if (generatorList.get(currentRow)[col] == 1) {
                            for (Integer[] integers : generatorList) {
                                int temp = integers[currentRow];
                                integers[currentRow] = integers[col];
                                integers[col] = temp;
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Generator matrix cannot be made systematic");
                    }
                }
                // at this point the current value is known to be 1
                for (int row = currentRow + 1; row < generatorList.size(); row++) {
                    if (generatorList.get(row)[currentRow] == 1) {
                        for (int col = currentRow; col < this.codewordLength; col++) {
                            generatorList.get(row)[col] ^= generatorList.get(currentRow)[col];
                        }
                    }
                }
                for (int row = currentRow + 1; row < generatorList.size(); row++) {
                    if (isAllZero(generatorList.get(row))) {
                        generatorList.remove(row);
                        row--;
                    }
                }
                currentRow++;
            }
            // now we have a systematic generator matrix
            int numRows = generatorList.size();
            int numCols = generatorList.getFirst().length;
            Codeword[] minGenerators = new Codeword[numRows];
            for (int i = 0; i < numRows; i++) {
                minGenerators[i] = new Codeword(numCols);
                for (int j = 0; j < numCols; j++) {
                    if (generatorList.get(i)[j] == 1) {
                        minGenerators[i].setBit(j);
                    } else {
                        minGenerators[i].clearBit(j);
                    }
                }
            }
            return new GeneratorMatrix(minGenerators);
        }
    }

    public static void main(String[] args) {
        // Example usage
        Codeword cw1 = new Codeword(10);
        cw1.setBit(0);
        cw1.setBit(1);
        System.out.println(cw1);
        Codeword cw2 = new Codeword(10);
        cw2.setBit(2);
        cw2.setBit(3);
        System.out.println(cw2);
        Codeword cw3 = new Codeword(10);
        cw3.setBit(4);
        cw3.setBit(5);
        System.out.println(cw3);
        Codeword cw4 = new Codeword(10);
        cw4.setBit(6);
        cw4.setBit(7);
        System.out.println(cw4);
        Codeword cw5 = new Codeword(10);
        cw5.setBit(8);
        cw5.setBit(9);
        System.out.println(cw5);

        GeneratorMatrix.Builder builder = new GeneratorMatrix.Builder(10);
        builder.addGeneratorRow(cw1)
                .addGeneratorRow(cw2)
                .addGeneratorRow(cw3)
                .addGeneratorRow(cw4)
                .addGeneratorRow(cw5);
        GeneratorMatrix gm = builder.buildSystematic();
        System.out.println("Generator Matrix:");
    }
}
