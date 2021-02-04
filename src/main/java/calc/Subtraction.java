package calc;

public class Subtraction extends Binary {
    public Subtraction(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitSubtraction(this);
    }
}
