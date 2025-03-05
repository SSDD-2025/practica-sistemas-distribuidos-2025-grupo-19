package ssdd.practicaWeb.dtosEdit;

public class Goal {
    private String goal;
    private boolean selected;

    public Goal() {
    }

    public Goal(String goal, boolean selected) {
        this.goal = goal;
        this.selected = selected;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
