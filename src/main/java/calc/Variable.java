package calc;

public class Variable implements Expression {
    final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }
}
