package calc;

public class Printer implements ExpressionVisitor<String> {
    public static String toString(Expression e) {
        return e.accept(new Printer());
    }

    private String binary(Binary binary, String symbol, boolean strict) {
        return brackets(binary, binary.lhs, false) + " " + symbol + " " + brackets(binary, binary.rhs, strict);
    }

    private String brackets(Expression outer, Expression inner, boolean strict) {
        String s = inner.accept(this);
        int outerRank = Ranker.rank(outer);
        int innerRank = Ranker.rank(inner);
        if (innerRank > outerRank || strict && innerRank == outerRank) {
            s = "(" + s + ")";
        }
        return s;
    }

    @Override
    public String visitDivision(Division division) {
        return binary(division, "/", true);
    }

    @Override
    public String visitMultiplication(Multiplication multiplication) {
        return binary(multiplication, "*", false);
    }

    @Override
    public String visitAddition(Addition addition) {
        return binary(addition, "+", false);
    }

    @Override
    public String visitSubtraction(Subtraction subtraction) {
        return binary(subtraction, "-", true);
    }

    @Override
    public String visitValue(Value value) {
        return Integer.toString(value.value);
    }

    @Override
    public String visitVariable(Variable variable) {
        return variable.name;
    }
}
