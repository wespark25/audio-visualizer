
package audioviz;

import javafx.scene.layout.AnchorPane;


public interface Visualizer {
    public void start(Integer numBands, AnchorPane vizPane);
    public void end();
    public String getName();
    public void visualize(double timestamp, double duration, float[] magnitudes, float[] phases);
}
