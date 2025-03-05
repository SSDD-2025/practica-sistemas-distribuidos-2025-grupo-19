package ssdd.practicaWeb.dtosEdit;

public class CaloricPhase {
    private String caloricPhase;
    private boolean selected;

    public CaloricPhase() {
    }

    public CaloricPhase(String caloricPhase, boolean selected) {
        this.caloricPhase = caloricPhase;
        this.selected = selected;
    }

    public String getCaloricPhase() {
        return caloricPhase;
    }

    public void setCaloricPhase(String caloricPhase) {
        this.caloricPhase = caloricPhase;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
