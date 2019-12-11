package audioviz;

import static java.lang.Integer.min;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class EllipseVisualizer4 implements Visualizer {

    private final String name = "EllipseVisualizer 4";

    private Integer numOfBands;
    private AnchorPane vizPane;

    private final Double bandHeightPercentage = 1.3;
    private final Double minEllipseRadius = 10.0;  // 10.0

    private Double width = 0.0;
    private Double height = 0.0;

    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;

    private final Double startHue = 160.0;
    
    Glow glow = new Glow();
    Reflection reflection = new Reflection();
    DropShadow dropShadow = new DropShadow();
    GaussianBlur gaus = new GaussianBlur();
    Blend blend = new Blend();
    Light.Distant light = new Light.Distant(); 

    private Rectangle[] bars;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();

        this.numOfBands = numBands;
        this.vizPane = vizPane;

        height = vizPane.getHeight();
        System.out.print(vizPane.getHeight());
        width = vizPane.getWidth();

        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        bars = new Rectangle[numBands];

        for (int i = 0; i < numBands; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(bandWidth / 2 + bandWidth * i);
            rectangle.setY(height / 2);
            rectangle.setWidth(bandWidth / 2);
            rectangle.setHeight(minEllipseRadius);
            rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            bars[i] = rectangle;
        }
    }

    @Override
    public void end() {
        if (bars != null) {
            for (Rectangle rectangle : bars) {
                vizPane.getChildren().remove(rectangle);
            }
            bars = null;
        }
    }

    @Override
    public void visualize(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (bars == null) {
            return;
        }

        Integer num = min(bars.length, magnitudes.length);
          
        for (int i = 0; i < num; i++) {
            bars[i].setY(-(((60.0 + magnitudes[i]) / 60.0) * halfBandHeight + minEllipseRadius) + height / 2);
            bars[i].setHeight(((60.0 + magnitudes[i]) / 60.0) * halfBandHeight + minEllipseRadius);
            bars[i].setFill(Color.hsb(startHue + (magnitudes[i]) * -6.0, 1.0, 1.0, 1.0));
            
            glow.setLevel(1.0);
            
            reflection.setBottomOpacity(0.0);
            reflection.setTopOpacity(0.5);
            reflection.setTopOffset(0.0);
            reflection.setFraction(0.7);
   
            dropShadow.setBlurType(BlurType.GAUSSIAN); 
            dropShadow.setColor(Color.BLACK); 
            dropShadow.setHeight(5); 
            dropShadow.setWidth(5); 
            dropShadow.setRadius(5); 
            dropShadow.setOffsetX(3); 
            dropShadow.setOffsetY(2); 
            dropShadow.setSpread(0); 

            light.setAzimuth(45.0); 
            light.setElevation(30.0);       
            Lighting lighting = new Lighting(); 
            lighting.setLight(light);  

            dropShadow.setInput(lighting);
            glow.setInput(dropShadow);
            reflection.setInput(glow);
            bars[i].setEffect(reflection);
            
           }

        }
    }
