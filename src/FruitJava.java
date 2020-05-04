import java.util.Scanner;

abstract class Fruit{

//    public int sum = 0;

    public String toString(){
        return "She bought most ";
    }

    public static void iAmBrainCalculator(Fruit[] fruits){

        int a[] = new int[3];
        for(int i = 0; i < fruits.length; i++){
            if(fruits[i] instanceof Apple){
                a[0] += ((Apple)fruits[i]).weight * ((Apple)fruits[i]).price/1000;
            }
            if(fruits[i] instanceof Pear){
                a[1] += ((Pear)fruits[i]).weight * ((Pear)fruits[i]).price/1000;
            }
            if(fruits[i] instanceof Banana){
                a[2] += ((Banana)fruits[i]).weight * ((Banana)fruits[i]).price/1000;
            }
        }
        int max = 0;
        int index = 0;
        int sum = 0;
        for(int i = 0; i < a.length; i++){
            if(a[i] > max){
                max = a[i];
                index = i;
            }
            sum += a[i];
        }
        if(index == 0){
            System.out.println(this.toString());
        }
        if(index == 1){
            System.out.println(this.toString("Pear!"));
        }
        if(index == 2){
            System.out.println(this.toString("Banana!"));
        }

        System.out.println("Total Price: " + sum);
    }

}

class Apple extends Fruit{
    public int price = 400;
    public int weight;



//    public Apple(){}

    public Apple(int weight){
        this.weight = weight;
    }

//    @Override
//    public String toString(){
//        return super.toString() + "Apple!";
//    }
}

class Pear extends Fruit{
    public int price = 600;
    public int weight;

//    public Pear(){}

    public Pear(int weight){
        this.weight = weight;
    }

//    @Override
//    public String toString(){
//        return super.toString() + "Apple!";
//    }
}
class Banana extends Fruit{
    public int price = 400;
    public int weight;

//    public Banana(){}

    public Banana(int weight){
        this.weight = weight;
    }

//    @Override
//    public String toString(){
//        return super.toString() + "Apple!";
//    }
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
