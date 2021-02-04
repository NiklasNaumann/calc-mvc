package calc;

public class Ranker implements ExpressionVisitor<Integer> {
    public static int rank(Expression e) {
        return e.accept(new Ranker());
    }

    @Override
    public Integer visitDivision(Division division) {
        return 1;
    }

    @Override
    public Integer visitMultiplication(Multiplication multiplication) {
        return 1;
    }

    @Override
    public Integer visitAddition(Addition addition) {
        return 2;
    }

    @Override
    public Integer visitSubtraction(Subtraction subtraction) {
        return 2;
    }

    @Override
    public Integer visitValue(Value value) {
        return 0;
    }

    @Override
    public Integer visitVariable(Variable variable) {
        return 0;
    }
}
