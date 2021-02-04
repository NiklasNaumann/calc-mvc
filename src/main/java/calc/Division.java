package calc;

public class Division extends Binary {
    public Division(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitDivision(this);
    }
}
