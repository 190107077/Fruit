public class Position {
    
    //data fields
    private int x;   
    private int y;

    //Constructor
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    //getter methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //setter methods
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // this methods cheks given position equals to the current position
    public boolean equals(Position position){

        return (position.getX() == x && position.getY() == y) ? true : false;
    }
}