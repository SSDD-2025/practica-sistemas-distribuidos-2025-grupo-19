package ssdd.practicaWeb.dtosEdit;

public class Gender {
    private String gender;
    private boolean selected;

    public Gender() {
    }

    public Gender(String gender, boolean selected) {
        this.gender = gender;
        this.selected = selected;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
