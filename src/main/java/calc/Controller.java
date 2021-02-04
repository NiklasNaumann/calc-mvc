package calc;

public class Controller {

    Model model;

    View view;

    public Controller(View view) {
        this.view = view;
        this.model = new Model();
        this.view.setModel(this.model);
        this.view.getCalculateButton().setOnAction(event -> this.model.setExpression(this.view.getInputTextField()));
    }
}
