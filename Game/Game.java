import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.List;


public class Game extends Application{

    //data fields
    private Map map;
    private Player player;
    private Food food;

	@Override
    public void start(Stage primaryStage) throws Exception{
        /*
         *
         */
        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();
        final String file = !parameters.isEmpty() ? parameters.get(0) : "";

	    map = new Map(file);
	    player = new MyPlayer(map);
	    food = new Food(map, player);

	    Scene scene = new Scene(map,map.getUnit()*map.getSize(), map.getUnit()*map.getSize());

        scene.setOnKeyPressed(event -> {
            KeyCode keyCode =  event.getCode();
            if(keyCode.equals(KeyCode.UP)){
                player.moveUp();
            }
            if(keyCode.equals(KeyCode.DOWN)){
                player.moveDown();
            }
            if(keyCode.equals(KeyCode.LEFT)){
                player.moveLeft();
            }
            if(keyCode.equals(KeyCode.RIGHT)){
                player.moveRight();
            }
        });

        primaryStage.setTitle("Eater");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    	}
	}
