package calc;

public interface ExpressionVisitor<T> {
    T visitDivision(Division division);

    T visitMultiplication(Multiplication multiplication);

    T visitAddition(Addition addition);

    T visitSubtraction(Subtraction subtraction);

    T visitValue(Value value);

    T visitVariable(Variable variable);
}
