//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Random;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Food {
    private Map map;
    private Pane foodPane;
    private Player player;
    private Circle circle;
    private Position foodPosition;
    private Label seconds;
    private final int timer = 5;
    private int numOfCircles = 10;
    private int time;
    private int points;
    private int size;

    public Food(Map map, Player player) {

        this.map = map;
        this.foodPane = new Pane();
        this.map.getChildren().add(this.foodPane);
        this.player = player;
        this.size = this.map.getSize();

        Thread var3 = new Thread(() -> {
            while(this.numOfCircles > 0) {
                this.createFood();
                Platform.runLater(() -> {
                    this.foodPane.getChildren().addAll(new Node[]{this.circle, this.seconds});
                });

                for(this.time = 5; this.time > 0; --this.time) {
                // for(this.time = 500; this.time > 0; --this.time) {
                    Platform.runLater(() -> {
                        this.seconds.setText("" + this.time);
                    });
                    if (this.player.getPosition().equals(this.foodPosition)) {
                        this.points += this.time;
                        break;
                    }

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException err) {
                    }
                }

                try {
                    Thread.sleep(10L);
                } catch (InterruptedException err) {
                }

                Platform.runLater(() -> {
                    this.foodPane.getChildren().clear();
                });
                --this.numOfCircles;
            }

            System.out.println(this.getPoints());
        });
        var3.start();
    }

    public int getPoints() {
        return this.points;
    }

    public Position getPosition() {
        return this.foodPosition;
    }

    private void createFood() {
        Random random = new Random();
        double unit = (double)this.map.getUnit();

        int x;
        int y;
        Position var6;
        do {
            do {
                x = random.nextInt(this.size);
                y = random.nextInt(this.size);
//                x = 9; y = 5;
//                x = 6; y = 0;
//                x=14;y=6;
//                x=1;y=14;
//                x=8;y=7;
//                x=11;y=9;
                var6 = new Position(x, y);
            } while(this.player.getPosition().equals(var6));
        } while(this.map.getValue(y, x) == 1);

        this.circle = new Circle((double)x * unit + unit / 2.0D, (double)y * unit + unit / 2.0D, unit / 4.0D);
        this.circle.setFill(Color.GREEN);
        this.foodPosition = var6;
        this.seconds = new Label("5");
        this.seconds.setTranslateX((double)x * unit);
        this.seconds.setTranslateY((double)y * unit);
    }
}
