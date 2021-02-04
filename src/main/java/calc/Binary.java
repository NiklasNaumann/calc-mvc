package calc;

public abstract class Binary implements Expression {
    final Expression lhs;
    final Expression rhs;

    public Binary(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
