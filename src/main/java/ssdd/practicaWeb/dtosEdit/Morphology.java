package ssdd.practicaWeb.dtosEdit;

public class Morphology {
    private String morphology;
    private boolean selected;

    public Morphology() {
    }

    public Morphology(String morphology, boolean selected) {
        this.morphology = morphology;
        this.selected = selected;
    }

    public String getMorphology() {
        return morphology;
    }

    public void setMorphology(String morphology) {
        this.morphology = morphology;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
