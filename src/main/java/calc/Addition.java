package calc;

public class Addition extends Binary {
    public Addition(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitAddition(this);
    }
}
