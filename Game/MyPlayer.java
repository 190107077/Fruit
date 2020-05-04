import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

class MyPlayer implements Player {
    //data fields
    private Rectangle player;
    private Map map;
    private Position position;

    //constructor
    public MyPlayer(Map map){
        Image image = new Image("player.png");

        this.map = map;
        position = map.getStartPosition();

        ImagePattern i = new ImagePattern(image, position.getX() * map.getUnit(), position.getX() * map.getUnit(), map.getUnit(), map.getUnit(), false);
        player = new Rectangle(position.getX() * map.getUnit(),position.getY() * map.getUnit(), map.getUnit(), map.getUnit());
        player.setFill(i);
        map.getChildren().add(player);

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
            map.getChildren().remove(player);
            position.setX(x + 1);
            player.setX((x + 1) * map.getUnit());
            map.getChildren().add(player);
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
            map.getChildren().remove(player);
            position.setX(x - 1);
            player.setX((x-1) * map.getUnit());
            map.getChildren().add(player);
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
            map.getChildren().remove(player);
            position.setY(y - 1);
            player.setY((y - 1) * map.getUnit());
            map.getChildren().add(player);
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
            map.getChildren().remove(player);
            position.setY(y + 1);
            player.setY((y + 1) * map.getUnit());
            map.getChildren().add(player);
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