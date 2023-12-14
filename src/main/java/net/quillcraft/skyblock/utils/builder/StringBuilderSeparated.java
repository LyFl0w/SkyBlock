package net.quillcraft.skyblock.utils.builder;

public class StringBuilderSeparated {

    private final String cuttingPoint;
    private final StringBuilder stringBuilder;

    public StringBuilderSeparated(StringBuilder stringBuilder, String cuttingPoint) {
        this.stringBuilder = stringBuilder;
        this.cuttingPoint = cuttingPoint;
    }

    public StringBuilderSeparated(String cuttingPoint) {
        this(new StringBuilder(), cuttingPoint);
    }

    public StringBuilderSeparated append(String append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(char append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(int append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(long append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(double append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(float append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(boolean append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    public StringBuilderSeparated append(Object append) {
        stringBuilder.append(cuttingPoint).append(append);
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
