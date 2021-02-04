package calc;

public class Value implements Expression {
    final int value;

    public Value(int value) {
        this.value = value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitValue(this);
    }
}
