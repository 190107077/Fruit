import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.atomic.AtomicInteger;

public class MyBotPlayer implements BotPlayer {
    private Map map;
    private Circle ball;
    private Position position;
    private Food food;

    private int size;
    private int find_count = 0;

    private int rating = 12;
    private int a;
    private int b;
    private int direction = -1;
//    private int botX; //= position.getX();
//    private int botY; //= position.getY();
//    private int foodX; //= food.getPosition().getX();
//    private int foodY; //= food.getPosition().getY();
    //Thread eating_bot;

    public MyBotPlayer(Map map) {

        this.map = map;
        position = map.getStartPosition();
        double r = map.getUnit() * 0.45;
        ball = new Circle(position.getX() * map.getUnit() + map.getUnit() / 2, position.getY() * map.getUnit() + map.getUnit() / 2, r, Color.RED);
        map.getChildren().add(ball);
        size = map.getSize();
    }

    @Override
    public void feed(Food f) {
        food = f;
    }

    @Override
    public void eat() {
        Thread eating_bot = new Thread(() -> {

            while (!(getPosition().equals(food.getPosition()))) {

              /*  бірінші бағытты анықтап алу керек
               *  food пен bot-тың координаттарын салыстыру керек
               */
                Long delay = 100L;
                int cycle = food.getPosition().getY() - this.getPosition().getY();
                if (cycle > 0) {
                    for (; cycle > 0; cycle--) {
                        this.moveDown();
                        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                } else {
                    for (; cycle < 0; cycle++) {
                        this.moveUp();
                        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
                cycle = food.getPosition().getX() - this.getPosition().getX();
                if (cycle > 0) {
                    for (; cycle > 0; cycle--) {
                        this.moveRight();
                        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                } else {
                    for (; cycle < 0; cycle++) {
                        this.moveLeft();
                        try { Thread.sleep(delay); } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }

                /* food табылды, енді 10 мс келесі тамақты күтеміз */
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        eating_bot.start();
    }

    private int delta(int x, int x0) {
        if (x0 > 0)
            return (x0 - x);
        if (x0 < 0)
            return x - x0;
        if (x > 0)
            return -x;
        return x;
    }

    @Override
    public void find() {
      /*
       *  Анализ бір қадамдап жүру үшін жасалды. Ең тупой логика деуге болады
       *  Статистика бойынша map3.txt картасы бойынша 90% жоғары жемтікті табады.
       *  Универсальный алгоритм ойлап табу үшін анализ жасағанда қадамдардың
       *  санын кем дегенде 4..5-ке арттыру керек
       */

        boolean debug = false;

        Thread eating_bot = new Thread(() -> {
            for (int count = 0; count < 10; count++) {
                do {

                    /*  бағытты анықтау  */
                    a = food.getPosition().getX() - position.getX(); //- батыс; + шығыс; 0 бір ендікте
                    b = food.getPosition().getY() - position.getY(); //- солтүстік; + оңтүстік; 0 бір бойлықта

                    // Жемтіктің табылғанын тексеру
                    if (a == 0 && b == 0) {
                        // counter - тапқандарымның саны
                        // бұл счетчик негізі Food-тың ішінде болуы керек. Оны өзгерте алмағандықтан
                        // жобалап есеп жүргіздім
//                        find_count++;
//                        System.out.println(find_count);
                        // Бағытты таңдауды қайта бастау
                        direction = -1;

                        try { Thread.sleep(100L); } catch (InterruptedException e) { e.printStackTrace(); }
                        break;
                    }

                    int x = position.getX();
                    int y = position.getY();

                    // for debug
                    //System.out.printf("m[%d][%d] = %d \n", y, x, m[y][x]);

                    int N = rating;
                    int E = rating;
                    int S = rating;
                    int W = rating;

                    /* N: Up
                     *
                     *    Егер алдымыздағы клетка бос болса, онда рейтингіні
                     *    көтереміз rating = size
                     *    Бос болмаса 0-ге теңейміз
                     */
                    if (map.getValueAt(x, y - 1) != 1) {
                        if (debug) System.out.printf("N = %d : b = %d\n", N, delta(food.getPosition().getY() - y + 1, b));
                        N += delta((food.getPosition().getY() - y + 1), b);
                    } else {
                        //System.out.printf("N: m(%d,%d) = %d\n", x, y - 1, map.getValueAt(x, y - 1));
                        N = 0;
                    }
                    if (map.getValueAt(x + 1, y) != 1) {
                        if (debug) System.out.printf("E = %d : a = %d\n", E, delta(food.getPosition().getX() - x - 1, a));
                        E += delta((food.getPosition().getX() - x - 1), a);
                    } else {
                        //System.out.printf("E: m(%d,%d) = %d\n", x + 1, y, map.getValueAt(x + 1, y));
                        E = 0;
                    }
                    if (map.getValueAt(x, y + 1) != 1) {
                        if (debug) System.out.printf("S = %d : b = %d\n", S, delta(food.getPosition().getY() - y - 1, b));
                        S += delta((food.getPosition().getY() - y - 1), b);
                    } else {
                        //System.out.printf("S: m(%d,%d) = %d\n", x, y + 1, map.getValueAt(x, y + 1));
                        S = 0;
                    }
                    if (map.getValueAt(x - 1, y) != 1) {
                        if (debug) System.out.printf("W = %d : a = %d\n", W, delta(food.getPosition().getX() - x + 1, a));
                        W += delta((food.getPosition().getX() - x + 1), a);
                    } else {
                        //System.out.printf("W: m(%d,%d) = %d\n", x - 1, y, map.getValueAt(x - 1, y));
                        W = 0;
                    }

                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                    }

                    /* direction - Келе жатқан бағытымызды көрсетеді
                     * егер бұл бірінші қадамымыз болса -1ге тең болады */
                    switch (direction) {
                        case 0:
                            N += 2;
                            S /= 2;   // Келе жатқан бағытты барынша өзгертпеуге тырысамыз
                            // сондықтан кері кетудің рейтингісін азайтамыз
                            break;
                        case 1:
                            E += 2;
                            W /= 2;
                            break;
                        case 2:
                            S += 2;
                            N /= 2;
                            break;
                        case 3:
                            W += 2;
                            E /= 2;
                            break;
                    }

                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за сохранение направления \n", N, E, S, W, direction);
                    }


                    /* Дәл алдымыз тупик болса, рейтингіні 0-ге теңейміз  */
                    if (map.getValueAt(x, y - 1) == 1) N = 0;
                    if (map.getValueAt(x + 1, y) == 1) E = 0;
                    if (map.getValueAt(x, y + 1) == 1) S = 0;
                    if (map.getValueAt(x - 1, y) == 1) W = 0;

                    /* Егер бір клеткадан кейін тупик болса рейтингті 2 есеге азайтамыз */
                    if(
                      map.getValueAt(x, y - 1)     != 1 &&
                      map.getValueAt(x - 1, y - 1) == 1 &&
                      map.getValueAt(x + 1, y - 1) == 1 &&
                      map.getValueAt(x, y - 2)     == 1
                      ) { N /= 2; }

                    if (
                      map.getValueAt(x + 1, y) != 1 &&
                      map.getValueAt(x + 2, y) == 1 &&
                      map.getValueAt(x + 1, y - 1) == 1 &&
                      map.getValueAt(x + 1, y + 1) == 1

                      ){  E /= 2; }

                    if (
                      map.getValueAt(x, y + 1) != 1 &&
                      map.getValueAt(x, y + 2) == 1 &&
                      map.getValueAt(x - 1, y + 1) == 1 &&
                      map.getValueAt(x + 1, y + 1) == 1

                      ){ S /= 2; }

                    if (
                      map.getValueAt(x - 1, y) != 1 &&
                      map.getValueAt(x - 2, y) == 1 &&
                      map.getValueAt(x - 1, y - 1) == 1 &&
                      map.getValueAt(x - 1, y + 1) == 1

                      ){ W /= 2; }

                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за избежания тупика \n", N, E, S, W, direction);
                    }


                    if (a > 0) E++;
                    if (a < 0) W++;
                    if (b > 0) S++;
                    if (b < 0) N++;

                    if (b == 0 && a > 0) { E++; }
                    if (b == 0 && a < 0) { W++; }
                    if (a == 0 && b < 0) { N++; }
                    if (a == 0 && b > 0) { S++; }

                    if (b == 0 && a == 1)  { E *= 2; }
                    if (b == 0 && a == -1) { W *= 2; }
                    if (a == 0 && b == 1)  { S *= 2; }
                    if (a == 0 && b == -1) { N *= 2; }

                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за приближения к цели \n", N, E, S, W, direction);
                    }

                    /*  Кедергіні айналып өту  */
                    if (direction == 0) {       // Up
                        if(map.getValueAt(x, y - 1) == 1) {
                            // target: x, y-2
                            if(
                            (
                              map.getValueAt(x-1, y) != 1 &&
                              map.getValueAt(x-1, y - 1) != 1 &&
                              map.getValueAt(x-1, y -2) != 1
                            ) ||
                            (
                              map.getValueAt(x-1, y) != 1 &&
                              map.getValueAt(x-2, y) != 1 &&
                              map.getValueAt(x-2, y -1) != 1 &&
                              map.getValueAt(x-2, y - 2) != 1)
                            ) { W += 4; }    // сол жағымен айналып өтуге болады

                            if(
                            (
                              map.getValueAt(x+1, y) != 1 &&
                              map.getValueAt(x+1, y - 1) != 1 &&
                              map.getValueAt(x+1, y -2) != 1 /*&& map.getValueAt(x, y - 2) != 1*/
                            ) ||
                            (
                              map.getValueAt(x+1, y) != 1 &&
                              map.getValueAt(x+2, y) != 1 &&
                              map.getValueAt(x+2, y -1) != 1 &&
                              map.getValueAt(x+2, y - 2) != 1)
                            ) { E += 4; }  // оң жағымен айналып өтуге болады

                        }
                    }
                    /*  Кедергіні айналып өту  */
                    if (direction == 1) {       // Right
                        if(map.getValueAt(x+1, y) == 1) {
                            // target: x+2, y
                            if(
                            (
                              map.getValueAt(x, y-1) != 1 &&
                              map.getValueAt(x+1, y - 1) != 1 &&
                              map.getValueAt(x+2, y -1) != 1 /*&& map.getValueAt(x+2, y) != 1*/
                            ) ||
                            (
                              map.getValueAt(x, y-1) != 1 &&
                              map.getValueAt(x, y - 2) != 1 &&
                              map.getValueAt(x+1, y -2) != 1 &&
                              map.getValueAt(x+2, y-2) != 1)
                            ) { N += 2; }   // сол жағымен айналып өтуге болады

                            if(
                            (
                              map.getValueAt(x, y+1) != 1 &&
                              map.getValueAt(x+1, y + 1) != 1 &&
                              map.getValueAt(x+2, y +1) != 1 /*&& map.getValueAt(x+2, y) != 1*/
                            ) ||
                            (
                              map.getValueAt(x, y+1) != 1 &&
                              map.getValueAt(x, y + 2) != 1 &&
                              map.getValueAt(x+1, y +2) != 1 &&
                              map.getValueAt(x+2, y+2) != 1)
                            ) { S += 2; }  // оң жағымен айналып өтуге болады

                        }
                    }
                    /*  Кедергіні айналып өту  */
                    if (direction == 2) {       // Down
                        if(map.getValueAt(x, y+1) == 1) {
                            // target: x, y+2
                            if(
                            (
                              map.getValueAt(x+1, y) != 1 &&
                              map.getValueAt(x+1, y + 1) != 1 &&
                              map.getValueAt(x+1, y +2) != 1 /*&& map.getValueAt(x, y+2) != 1*/
                            ) ||
                            (
                              map.getValueAt(x+1, y) != 1 &&
                              map.getValueAt(x+2, y) != 1 &&
                              map.getValueAt(x+2, y +1) != 1 &&
                              map.getValueAt(x+2, y+2) != 1)
                            ) { E += 2; }     // сол жағымен айналып өтуге болады

                            if(
                            (
                              map.getValueAt(x-1, y) != 1 &&
                              map.getValueAt(x-1, y + 1) != 1 &&
                              map.getValueAt(x-1, y +2) != 1 /*&& map.getValueAt(x, y+2) != 1*/
                            ) ||
                            (
                              map.getValueAt(x-1, y) != 1 &&
                              map.getValueAt(x-2, y) != 1 &&
                              map.getValueAt(x-2, y +1) != 1 &&
                              map.getValueAt(x-2, y-2) != 1)
                            ) { W += 2; }   // оң жағымен айналып өтуге болады

                        }
                    }
                    /*  Кедергіні айналып өту  */
                    if (direction == 3) {       // Left
                        if(map.getValueAt(x-1, y) == 1) {
                            // target: x-2, y
                            if(
                            (
                              map.getValueAt(x, y+1) != 1 &&
                              map.getValueAt(x-1, y + 1) != 1 &&
                              map.getValueAt(x-2, y +1) != 1 /*&& map.getValueAt(x-2, y) != 1*/
                            ) ||
                            (
                              map.getValueAt(x, y+1) != 1 &&
                              map.getValueAt(x, y + 2) != 1 &&
                              map.getValueAt(x-1, y +2) != 1 &&
                              map.getValueAt(x-2, y+2) != 1)
                            ) { S += 2; }      // сол жағымен айналып өтуге болады

                            if(
                            (
                              map.getValueAt(x, y-1) != 1 &&
                              map.getValueAt(x-1, y - 1) != 1 &&
                              map.getValueAt(x-2, y -1) != 1 /*&& map.getValueAt(x-2, y) != 1*/
                            ) ||
                            (
                              map.getValueAt(x, y-1) != 1 &&
                              map.getValueAt(x, y - 2) != 1 &&
                              map.getValueAt(x-1, y -2) != 1 &&
                              map.getValueAt(x-2, y-2) != 1)
                            ){ N += 2; }       // оң жағымен айналып өтуге болады
                        }
                    }

                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за объезд препятствия \n", N, E, S, W, direction);
                    }

                    //System.out.printf("N = %d, E = %d, S = %d, W = %d%n", N, E, S, W);
                    if (debug)
                        System.out.println("---------------------------------------------------------------------------------");

                    /* Қай бағытқа жүретінімізді таңдау */
                    if (N >= E && N >= S && N >= W && y != 0) {
                        if (debug) {
                            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y - 1);
                            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                        }
                        direction = 0;
                    } else if (E >= N && E >= S && E >= W && x != size) {
                        if (debug) {
                            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x + 1, y);
                            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                        }
                        direction = 1;
                    } else if (S >= N && S >= E && S >= W && y != size) {
                        if (debug) {
                            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y + 1);
                            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                        }
                        direction = 2;
                    } else if (W >= N && W >= E && W >= S && x != 0) {
                        if (debug) {
                            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x - 1, y);
                            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                        }
                        direction = 3;
                    }

                    //***************************************************************************************
                    switch (direction) {
                        case 0:
                            moveUp();
                            try { Thread.sleep(100L); } catch (InterruptedException e) { e.printStackTrace(); }
                            break;
                        case 1:
                            moveRight();
                            try { Thread.sleep(100L); } catch (InterruptedException e) { e.printStackTrace(); }
                            break;
                        case 2:
                            moveDown();
                            try { Thread.sleep(100L); } catch (InterruptedException e) { e.printStackTrace(); }
                            break;
                        case 3:
                            moveLeft();
                            try { Thread.sleep(100L); } catch (InterruptedException e) { e.printStackTrace(); }
                            break;
                        default:
                            System.out.println(direction);
                    }


                } while (true && !debug);

                try { Thread.sleep(800); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });
        eating_bot.start();
    }

    @Override
    public synchronized void moveRight() {
        int x = position.getX();
        int y = position.getY();

        if ((x + 1) < size && map.getValueAt(x + 1, y) != 1) {
            Platform.runLater(() -> {
                map.getChildren().remove(ball);
                position.setX(x + 1);
                ball.setCenterX((x + 1) * map.getUnit() + map.getUnit() / 2);
                map.getChildren().add(ball);
            });
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y, x+1, map.getValueAt(x+1,y));
        }
    }

    @Override
    public synchronized void moveLeft() {
        int x = position.getX();
        int y = position.getY();


        if ((x - 1) >= 0 && map.getValueAt(x - 1, y) != 1) {
            Platform.runLater(() -> {
                map.getChildren().remove(ball);
                position.setX(x - 1);
                ball.setCenterX((x - 1) * map.getUnit() + map.getUnit() / 2);
                map.getChildren().add(ball);
            });
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y, x-1, map.getValueAt(x-1,y));
        }

    }

    @Override
    public synchronized void moveUp() {
        int x = position.getX();
        int y = position.getY();

        if ((y - 1) >= 0 && map.getValueAt(x, y - 1) != 1) {
            Platform.runLater(() -> {
                map.getChildren().remove(ball);
                position.setY(y - 1);
                ball.setCenterY((y - 1) * map.getUnit() + map.getUnit() / 2);
                map.getChildren().add(ball);
            });
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y-1, x, map.getValueAt(x,y-1));
        }
    }

    @Override
    public synchronized void moveDown() {
        int x = position.getX();
        int y = position.getY();

        if ((y + 1) < size && map.getValueAt(x, y + 1) != 1) {
            Platform.runLater(() -> {
                map.getChildren().remove(ball);
                position.setY(y + 1);
                ball.setCenterY((y + 1) * map.getUnit() + map.getUnit() / 2);
                map.getChildren().add(ball);
            });
            // for debug
            //System.out.printf("map[%d][%d] = %d\n", y+1, x, map.getValueAt(x,y+1));
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
