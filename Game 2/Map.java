import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Map extends Pane {
    // data fields
    private int UNIT = 40;  // size of one cells
    private int size;       // size of map
    private int[][] map;    // two-dimensional array to keep data int[y][x]
    private Position start;  

    // constructor
    public Map(String file) throws FileNotFoundException {

        Scanner read = new Scanner(new File(file));
        size = Integer.parseInt(read.nextLine());
        System.out.println("Map size: " + size);

        map = new int[size][size];

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = read.nextInt();
                if(map[i][j] == 2)
                    start = new Position(j,i);
//                    start = new Position(5,5);
//                    start = new Position(10,0);
//                    start = new Position(1,4);
//                    start = new Position(9,12);
            }
        }

        for(int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                Rectangle a = new Rectangle(x * UNIT, y * UNIT, UNIT, UNIT);
                if(map[y][x] == 1) {
                    a.setFill(Color.BLACK);
                } else {
                    a.setFill(Color.WHITE);
                }
                a.setStroke(Color.BLACK);
                getChildren().add(a);
            }
        }
    }

    //getter methods
    public int getUnit(){
        return UNIT;
    }

    public int getSize() {
        return size;
    }

    //That method is needed to check the value of the map at a certain row and column
    public int getValueAt(int x, int y) {
        // х пен у-тің шекарасын тексеру керек, шекарадан шыққан жағдайда 1 қайтаруы керек
        if (x >= 0 && x < size && y >= 0 && y < size)
            return map[y][x];
        return 1;
    }

    public int getValue(int y, int x) {
        // Food осы методты пайдаланады
        // сол кластың ішінде х пен у-тің орындары ауысып кеткен
        return map[y][x];
    }

    //find start pposition of player
    public Position getStartPosition(){
        return start;
    }
}