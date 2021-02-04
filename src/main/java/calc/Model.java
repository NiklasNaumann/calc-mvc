package calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Model {

    private ArrayList<View> obervers = new ArrayList<>();

    private HashMap<String, Integer> variables;

    private String input;
    private Expression parsedExpression;
    private String serializedExpression;
    private int evaluatedExpression;
    private boolean parserThrewException;
    
    private Optional<String> serializedVariable;
    private Optional<String> exceptionMessage;

    public Model() {
        this.variables = new HashMap<>();
        this.exceptionMessage = Optional.empty();
    }

    public String getInput() {
        return this.input;
    }

    public String getSerializedExpression() {
        return this.serializedExpression;
    }

    public int getEvaluatedExpression() {
        return this.evaluatedExpression;
    }

    public HashMap<String, Integer> getVariables() {
        return this.variables;
    }

    public Optional<String> getExceptionMessage() {
        return this.exceptionMessage;
    }

    public Optional<String> getParsedVariable(){
        return this.serializedVariable;
    }

    public boolean getParserThrewExceptionMessage(){
        return this.parserThrewException;
    }

    public void setExpression(String input) {
        this.input = input;
        if (input.contains(":=")) {
            String[] parts = input.split(":=");

            // Evaluate the value of the variable
            eval(parts[1]);

            // Add variable to variables. Removes all whitespaces behind its name.
            this.variables.put(parts[0].trim(), this.evaluatedExpression);
            this.serializedVariable = Optional.of(parts[0].trim() + " := " + this.evaluatedExpression);
        } else {
            eval(input);
            this.serializedVariable = Optional.empty();
        }
        notifyObservers();
    }

    public void attach(View observer) {
        this.obervers.add(observer);
    }

    public void detach(View observer) {
        this.obervers.remove(observer);
    }

    public void notifyObservers() {
        for (View observer : obervers) {
            observer.update(this);
        }
    }

    private void eval(String input) {
        try {
            this.parsedExpression = Parser.parse(input);
            this.exceptionMessage = Optional.empty();
        } catch (CalcException exception) {
            this.exceptionMessage = Optional.of(exception.getMessage());
            this.parserThrewException = true;
            return;
        }
        this.serializedExpression = Printer.toString(this.parsedExpression);

        try {
            this.evaluatedExpression = Evaluator.evaluate(this.parsedExpression, this.variables);
            this.exceptionMessage = Optional.empty();
        } catch (CalcException exception) {
            this.parserThrewException = false;
            this.exceptionMessage = Optional.of(exception.getMessage());
        }
    }
}
