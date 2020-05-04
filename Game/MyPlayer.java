import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class MyPlayer implements Player {
    //data fields
    private Circle ball;
    private Map map;
    private Position position;

    //constructor
    public MyPlayer(Map map){
        this.map = map;
        position = map.getStartPosition();
        double r = map.getUnit() * 0.45;
        ball = new Circle(position.getX() * map.getUnit() + map.getUnit() / 2,position.getY() * map.getUnit() + map.getUnit() / 2,r, Color.RED);
        map.getChildren().add(ball);

        int x = position.getX();
        int y = position.getY();
        // for debug
        //System.out.printf("map[%d][%d] = %d\n", y, x, map.getValueAt(y,x));

    }

    //implementing methods
    @Override
    public void moveRight() {
        int x = position.getX();
        int y = position.getY();
        int size = map.getSize();

        if ((x + 1) < size && map.getValueAt(x+1,y) != 1 ) {
            map.getChildren().remove(ball);
            position.setX(x + 1);
            ball.setCenterX((x + 1) * map.getUnit() + map.getUnit() / 2);
            map.getChildren().add(ball);
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y, x+1, map.getValueAt(x+1,y));
        } else {
            System.out.println("Invalid position");
        }
    }

    @Override
    public void moveLeft() {
        int x = position.getX();
        int y = position.getY();

        if ((x - 1) >= 0 && map.getValueAt(x-1,y) != 1 ) {
            map.getChildren().remove(ball);
            position.setX(x - 1);
            ball.setCenterX((x-1) * map.getUnit() + map.getUnit() / 2);
            map.getChildren().add(ball);
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y, x-1, map.getValueAt(x-1,y));
        } else {
            System.out.println("Invalid position");
        }

    }

    @Override
    public void moveUp() {
        int x = position.getX();
        int y = position.getY();

        if ((y - 1) >= 0 && map.getValueAt(x,y-1) != 1 ) {
            map.getChildren().remove(ball);
            position.setY(y - 1);
            ball.setCenterY((y - 1) * map.getUnit() + map.getUnit() / 2);
            map.getChildren().add(ball);
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y-1, x, map.getValueAt(x,y-1));
        } else {
            System.out.println("Invalid position");
        }
    }

    @Override
    public void moveDown() {
        int x = position.getX();
        int y = position.getY();
        int size = map.getSize();

        if ((y + 1) < size && map.getValueAt(x,y+1) != 1 ) {
            map.getChildren().remove(ball);
            position.setY(y + 1);
            ball.setCenterY((y + 1) * map.getUnit() + map.getUnit() / 2);
            map.getChildren().add(ball);
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y+1, x, map.getValueAt(x,y+1));
        } else {
            System.out.println("Invalid position");
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }
}