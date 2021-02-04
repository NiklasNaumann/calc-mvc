package calc;

import java.util.Map;
import java.util.function.BiFunction;

public class Evaluator implements ExpressionVisitor<Integer> {
    private final Map<String, Integer> variables;

    public Evaluator(Map<String, Integer> variables) {
        this.variables = variables;
    }

    public static int evaluate(Expression e, Map<String, Integer> variables) {
        return e.accept(new Evaluator(variables));
    }

    private int binary(Binary binary, BiFunction<Integer, Integer, Integer> f) {
        return f.apply(binary.lhs.accept(this), binary.rhs.accept(this));
    }

    @Override
    public Integer visitDivision(Division division) {
        return binary(division, (a, b) -> a / b);
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication) {
        return binary(multiplication, (a, b) -> a * b);
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return binary(addition, (a, b) -> a + b);
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction) {
        return binary(subtraction, (a, b) -> a - b);
    }

    @Override
    public Integer visitValue(Value value) {
        return value.value;
    }

    @Override
    public Integer visitVariable(Variable variable) {
        String name = variable.name;
        if (!variables.containsKey(name)) {
            throw new CalcException("Unknown variable: " + name);
        }
        return variables.get(name);
    }
}
