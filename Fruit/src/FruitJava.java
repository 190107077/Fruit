import java.util.Scanner;

abstract class Fruit{

    public abstract String toString();      //осы методты пайдалану үшін абстракт қыламыз

    public abstract int getWeight();        //басқа сабкласстардан салмақты алу үшін

    public abstract int getPrice();         //басқа сабкласстардан бағасын алу үшін

    public static void iAmBrainCalculator(Fruit[] fruits){

        /*  Fruit массивін қабылдап алады
         *  сосын типтері бірдей элементердің бағасын есептеп жинақтайды
         *  жинақталғандардың ішінен ең үлкенін анықтайды
         *  сосын суммасы шығарады
         *  сол типтің toString методын шақырады
         */
        float a[] = new float[3];

        int index = 0;
        float max = 0;
        float sum = 0;

        Fruit f;
        for (int i = 0; i < fruits.length; i++) {

          f = fruits[i];

          if ( f instanceof Apple ) {

            a[0] += f.getWeight() * f.getPrice() / 1000F;   // дұрыс есептеу үшін айнымалылырды float типінде алдық
            if ( max < a[0] ) {
              max = a[0];
              index = i;
              }
          }

          if ( f instanceof Pear) {
            a[1] += f.getWeight() * f.getPrice() / 1000F;
            if(max < a[1]) {
              max = a[1];
              index = i;
            }
          }

          if ( f instanceof Banana) {
            a[2] += f.getWeight() * f.getPrice() / 1000F;
            if(max < a[2]) {
              max = a[2];
              index = i;
            }
          }
        }

        sum = a[0] + a[1] + a[2];

        System.out.println(fruits[index]);
        System.out.println("Apple " + a[0]); // Тексеру үшін
        System.out.println("Pear " + a[1]);
        System.out.println("Banana " + a[2]);

        System.out.println("Total Price: " + Math.round(sum)); // шыққан бағаны бүтін санға айналдыру үшін

        System.out.println("Total Price: " + sum);
    }

}

class Apple extends Fruit{

    public int price = 423;
    public int weight;

    public Apple(int weight){
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "She bought most Apple";
    }
    @Override
    public int getWeight(){
      return weight;
    }
    public int getPrice(){
      return price;
    }
}

class Pear extends Fruit{

    public int price = 612;
    public int weight;

    public Pear(int weight){
        this.weight = weight;

    }

    @Override
    public String toString() {
        return "She bought most Pear";
    }

    @Override
    public int getWeight(){
      return weight;
    }
    public int getPrice(){
      return price;
    }
}
class Banana extends Fruit{
    public int price = 350;
    public int weight;

    public Banana(int weight){
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "She bought most Banana";
    }

    @Override
    public int getWeight(){
      return weight;
    }
    public int getPrice(){
      return price;
    }
}

public class FruitJava {
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        Fruit[] fruits = new Fruit[n];
        int type;

        for(int i = 0; i < n; i++){
            type = in.nextInt();

            switch(type){
                case 0:
                    fruits[i] = new Apple(in.nextInt());
                    break;
                case 1:
                    fruits[i] = new Pear(in.nextInt());
                    break;
                case 2:
                    fruits[i] = new Banana(in.nextInt());
                    break;
            }
        }


        Fruit.iAmBrainCalculator(fruits);
    }
}
