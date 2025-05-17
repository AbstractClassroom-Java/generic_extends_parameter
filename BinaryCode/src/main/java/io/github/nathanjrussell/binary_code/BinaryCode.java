package io.github.nathanjrussell.binary_code;

import java.util.ArrayList;
import java.util.List;

public class BinaryCode {

    int codewordLength;
    int codeDimension;
    List<Codeword> generators;

    BinaryCode(int codewordLength, int dimension) {
        this.codewordLength = codewordLength;
        generators = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            generators.add(new Codeword(codewordLength));
        }
    }

    BinaryCode(int codewordLength) {
        this.codewordLength = codewordLength;
        generators = new ArrayList<>();
    }

    BinaryCode(List<Codeword> generators) {
        this.codewordLength = generators.getFirst().getLength();
        this.generators = new ArrayList<>(generators);
    }

    public int getCodewordLength() {
        return codewordLength;
    }

    public void addGeneratorRow(Codeword generator) {
        generators.add(generator);
    }

    public void updateGeneratorRow(int index, Codeword generator) {
        generators.set(index, generator);
    }

    public Codeword getGeneratorRow(int index) {
        return generators.get(index);
    }

    public List<Codeword> getGenerators() {
        return generators;
    }


}
