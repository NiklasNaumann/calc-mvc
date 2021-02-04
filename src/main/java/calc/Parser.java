package calc;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final String string;
    private int pos = 0;

    private Parser(String string) {
        this.string = string;
    }

    public static Expression parse(String string) {
        Parser p = new Parser(string);
        Optional<Expression> result = p.expression();
        p.whitespace();
        if (result.isEmpty() || p.pos != string.length()) {
            throw new CalcException("Invalid expression " + string);
        }
        return result.get();
    }

    private void whitespace() {
        Pattern p = Pattern.compile("^\\s*");
        Matcher m = p.matcher(string);
        m.region(pos, string.length());
        if (m.find()) {
            pos = m.end();
        }
    }

    private Optional<String> token(String regex) {
        whitespace();
        Pattern p = Pattern.compile("^" + regex);
        Matcher m = p.matcher(string);
        m.region(pos, string.length());
        if (m.find()) {
            pos = m.end();
            return Optional.of(string.substring(m.start(), m.end()));
        }
        return Optional.empty();
    }

    private Optional<String> identifier() {
        return token("[a-zA-Z_][0-9a-zA-Z]*");
    }

    private Optional<Integer> integer() {
        return token("-?[0-9]+").map(Integer::parseInt);
    }

    private Optional<Expression> variable() {
        return identifier().map(Variable::new);
    }

    private Optional<Expression> value() {
        return integer().map(Value::new);
    }

    private Optional<Expression> brackets() {
        return token("\\(").flatMap(t -> expression()).flatMap(t -> {
            if (token("\\)").isPresent()) {
                return Optional.of(t);
            }
            return Optional.empty();
        });
    }

    private Optional<Expression> unary() {
        return variable().or(this::value).or(this::brackets);
    }

    private Optional<Expression> multiplication(Expression lhs) {
        if (token("\\*").isPresent()) {
            return unary().map(rhs -> new Multiplication(lhs, rhs));
        }
        return Optional.empty();
    }

    private Optional<Expression> division(Expression lhs) {
        if (token("\\/").isPresent()) {
            return unary().map(rhs -> new Division(lhs, rhs));
        }
        return Optional.empty();
    }

    private Optional<Expression> termOperand(Expression lhs) {
        return multiplication(lhs).or(() -> division(lhs));
    }

    private Optional<Expression> term() {
        Optional<Expression> result = unary();
        Optional<Expression> next = result;
        while (next.isPresent()) {
            result = next;
            next = termOperand(result.get());
        }
        return result;
    }

    private Optional<Expression> addition(Expression lhs) {
        if (token("\\+").isPresent()) {
            return term().map(rhs -> new Addition(lhs, rhs));
        }
        return Optional.empty();
    }

    private Optional<Expression> subtraction(Expression lhs) {
        if (token("\\-").isPresent()) {
            return term().map(rhs -> new Subtraction(lhs, rhs));
        }
        return Optional.empty();
    }

    private Optional<Expression> expressionOperand(Expression lhs) {
        return addition(lhs).or(() -> subtraction(lhs));
    }

    private Optional<Expression> expression() {
        Optional<Expression> result = term();
        Optional<Expression> next = result;
        while (next.isPresent()) {
            result = next;
            next = expressionOperand(result.get());
        }
        return result;
    }
}
