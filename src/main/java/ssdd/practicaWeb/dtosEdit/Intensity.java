package ssdd.practicaWeb.dtosEdit;

public class Intensity {
    private String intensity;
    private boolean selected;

    public Intensity() {
    }

    public Intensity(String intensity, boolean selected) {
        this.intensity = intensity;
        this.selected = selected;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
