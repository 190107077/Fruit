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
    private int count = 10;
    private int[][] m;
    private int[] stack;
    int a;
    int b;
    int direction = -1;
    int botX; //= position.getX();
    int botY; //= position.getY();
    int foodX; //= food.getPosition().getX();
    int foodY; //= food.getPosition().getY();
    //Thread eating_bot;

    public MyBotPlayer(Map map) {

        this.map = map;
        position = map.getStartPosition();
        double r = map.getUnit() * 0.45;
        ball = new Circle(position.getX() * map.getUnit() + map.getUnit() / 2, position.getY() * map.getUnit() + map.getUnit() / 2, r, Color.RED);
        map.getChildren().add(ball);


        /* Уақытша тұр деп ойлаймын
         * map-тың копиясы          */
        size = map.getSize();
        m = new int[size][size];
        stack = new int[size * size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf(" %d  ", map.getValueAt(i, j));
                m[i][j] = 0;
            }
            System.out.println();
        }


    }

    @Override
    public void feed(Food f) {
        food = f;

    }

    @Override
    public void eat() {
        Thread eating_bot = new Thread(() -> {
            while (!getPosition().equals(food.getPosition())) {
                /*  бірінші бағытты анықтап алу керек
                 *  food пен bot-тың координаттарын салыстыру керек
                 */
                Long delay = 100L;
                int cycle = food.getPosition().getY() - this.getPosition().getY();
                if (cycle > 0) {
                    for (; cycle > 0; cycle--) {
                        // төмен қарай жүреміз
                        //Platform.runLater(() -> {
                        this.moveDown();
                        //});

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (; cycle < 0; cycle++) {
                        // жоғары қарай жүреміз
//                        Platform.runLater(() -> {
                        this.moveUp();
//                        });

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                cycle = food.getPosition().getX() - this.getPosition().getX();
                if (cycle > 0) {
                    for (; cycle > 0; cycle--) {
                        // оңға қарай жүреміз
//                        Platform.runLater(() -> {
                        this.moveRight();
//                        });

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    for (; cycle < 0; cycle++) {
                        // солға қарай жүреміз
//                        Platform.runLater(() -> {
                        this.moveLeft();
//                        });

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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


    private int rating(int direction) {

        /*
         *  . . . X X . . .
         *  . . X . . X . .
         *  2 . . . . . . .
         *  X . X . . X . X
         *  X . . . . . . X
         *  . . . . . - . .
         *  . . X . . X . .
         *  . . . X X . . .
         *
         (y,x) (b,a)

               -1,+0
        +0,-1         +0,+1
               +1,+0
        m[
         */
        int x = position.getX();
        int y = position.getY();

        int N = 0;
        int E = 0;
        int S = 0;
        int W = 0;

        if (map.getValueAt(x, y - 1) != 1) {
            N = x + y;
            System.out.printf("N = %d : b = %d: %d\n", N, food.getPosition().getY() - y + 1, (food.getPosition().getY() - y + 1) - (b));
            N -= ((food.getPosition().getY() - y + 1) - (b));
        } else {
            System.out.printf("N: m(%d,%d) = %d\n", x, y - 1, map.getValueAt(x, y - 1));
            N = 0;
        }
        if (map.getValueAt(x + 1, y) != 1) {
            E = x + y;
            System.out.printf("E = %d : a = %d: %d\n", E, food.getPosition().getX() - x - 1, (food.getPosition().getX() - x - 1) - (a));
            E -= ((food.getPosition().getX() - x - 1) - (a));
        } else {
            System.out.printf("E: m(%d,%d) = %d\n", x + 1, y, map.getValueAt(x + 1, y));
            E = 0;
        }
        if (map.getValueAt(x, y + 1) != 1) {
            S = x + y;
            System.out.printf("S = %d : b = %d: %d\n", S, food.getPosition().getY() - y - 1, (food.getPosition().getY() - y - 1) - (b));
            S -= ((food.getPosition().getY() - y - 1) - (b));
        } else {
            System.out.printf("S: m(%d,%d) = %d\n", x, y + 1, map.getValueAt(x, y + 1));
            S = 0;
        }
        if (map.getValueAt(x - 1, y) != 1) {
            W = x + y;
            System.out.printf("W = %d : a = %d: %d\n", W, food.getPosition().getX() - x + 1, (food.getPosition().getX() - x + 1) - (a));
            W -= ((food.getPosition().getX() - x + 1) - (a));
        } else {
            System.out.printf("N: m(%d,%d) = %d\n", x - 1, y, map.getValueAt(x - 1, y));
            W = 0;
        }

        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);

        switch (direction) {
            case 0:
                S /= 2;
                N++;
                break;
            case 1:
                W /= 2;
                E++;
                break;
            case 2:
                N /= 2;
                S++;
                break;
            case 3:
                E /= 2;
                W++;
                break;
        }


        //System.out.printf("N = %d, E = %d, S = %d, W = %d%n", N, E, S, W);
        System.out.println("-----------------");

        if (N >= E && N >= S && N >= W) {
            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y - 1);
            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
            return 0;
        }
        if (E >= N && E >= S && E >= W) {
            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x + 1, y);
            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
            return 1;
        }
        if (S >= N && S >= E && S >= W) {
            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y + 1);
            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
            return 2;
        }
        if (W >= N && W >= E && W >= S) {
            System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x - 1, y);
            System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
            return 3;
        }

        return -1;
    }

    @Override
    public void find() {
        /*
         *  Анализ бір қадамдап жүру үшін жасалды. Ең тупой логика деуге болады
         *  Статистика бойынша map3.txt картасы бойынша 90% жемтікті табады.
         *  Универсальный алгоритм ойлап табу үшін анализ жасағанда қадамдардың
         *  санын кем дегенде 4..5-ке арттыру керек
         */

        boolean debug = false;

        Thread eating_bot = new Thread(() -> {
            for (count = 0; count < 10; count++) {
//            for (count = 0; count < 1; count++) {
                do {
                    botX = position.getX();
                    botY = position.getY();
                    foodX = food.getPosition().getX();
                    foodY = food.getPosition().getY();

                    /*  бағытты анықтау  */
                    a = foodX - botX; //- батыс + шығыс 0 бір ендікте
                    b = foodY - botY; //- солтүстік + оңтүстік 0 бір бойлықта

                    // Жемтіктің табылғанын тексеру
                    if (a == 0 && b == 0) {
                        direction = -1;
                        // жүрген жерлерді тазалау
//                    for(int i=0; i<size; i++)
//                        for (int j=0; j<size; j++)
//                            m[i][j] = 0;

                        //System.out.println("***************************************************************");
                        //System.out.printf("**   %d              Келесі жемтікті іздеу                   **\n", count);
                        //System.out.println("***************************************************************");
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }


                    /*
                     *  солтүстік           0,-y    N
                     *  солтүстік-шығыс     x,-y    NE
                     *  шығыс               x,0     E
                     *  оңтүстік-шығыс      x,y     SE
                     *  оңтүстік            0,y     S
                     *  оңтүстік-батыс      -x,y    SW
                     *  батыс               -x,0    W
                     *  солтүстік-батыс     -x,-y   NW
                     */
                    String orient = "N|NE|E|SE|S|SW|W|NW";
                    // бағыттарды бағалаймыз
                    // функциядан не аламыз:    raing[N=0], rating[E=1], rating[S=2], rating[W=3]
                    ;
                    //***************************************************************************************
                    int x = position.getX();
                    int y = position.getY();

                    // осы жерде болдым
//                m[y][x] = 1;
                    //System.out.printf("m[%d][%d] = %d \n", y, x, m[y][x]);

                    int N = 0;
                    int E = 0;
                    int S = 0;
                    int W = 0;

                    if (map.getValueAt(x, y - 1) != 1) {
                        N = x + y;
                        if (debug)
                            System.out.printf("N = %d : b = %d\n", N, /*food.getPosition().getY() - y + 1,*/ delta(food.getPosition().getY() - y + 1, b));
                        N += delta((food.getPosition().getY() - y + 1), b);
                    } else {
                        //System.out.printf("N: m(%d,%d) = %d\n", x, y - 1, map.getValueAt(x, y - 1));
                        N = 0;
                    }
                    if (map.getValueAt(x + 1, y) != 1) {
                        E = x + y;
                        if (debug)
                            System.out.printf("E = %d : a = %d\n", E, /*food.getPosition().getX() - x - 1,*/ delta(food.getPosition().getX() - x - 1, a));
                        E += delta((food.getPosition().getX() - x - 1), a);
                    } else {
                        //System.out.printf("E: m(%d,%d) = %d\n", x + 1, y, map.getValueAt(x + 1, y));
                        E = 0;
                    }
                    if (map.getValueAt(x, y + 1) != 1) {
                        S = x + y;
                        if (debug)
                            System.out.printf("S = %d : b = %d\n", S, /*food.getPosition().getY() - y - 1,*/ delta(food.getPosition().getY() - y - 1, b));
                        S += delta((food.getPosition().getY() - y - 1), b);
                    } else {
                        //System.out.printf("S: m(%d,%d) = %d\n", x, y + 1, map.getValueAt(x, y + 1));
                        S = 0;
                    }
                    if (map.getValueAt(x - 1, y) != 1) {
                        W = x + y;
                        if (debug)
                            System.out.printf("W = %d : a = %d\n", W, /*food.getPosition().getX() - x + 1,*/ delta(food.getPosition().getX() - x + 1, a));
                        W += delta((food.getPosition().getX() - x + 1), a);
                    } else {
                        //System.out.printf("W: m(%d,%d) = %d\n", x - 1, y, map.getValueAt(x - 1, y));
                        W = 0;
                    }
                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
                    }
                    switch (direction) {
                        case 0:
                            S /= 2;
//                            S -= 3;
                            N += 2;
                            break;
                        case 1:
                            W /= 2;
//                            W -= 3;
                            E += 2;
                            break;
                        case 2:
                            N /= 2;
//                            N -= 3;
                            S += 2;
                            break;
                        case 3:
                            E /= 2;
//                            E -= 3;
                            W += 2;
                            break;
                    }

                    //System.out.println("-----------------  direction  -----------------");
                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за сохранение направления \n", N, E, S, W, direction);
                    }

                    if (map.getValueAt(x, y - 1) == 1) N = 0;
                    if (map.getValueAt(x, y - 1) != 1 && map.getValueAt(x, y - 2) == 1 && map.getValueAt(x - 1, y - 1) == 1 && map.getValueAt(x + 1, y - 1) == 1)
                        if (N > 4) N /= 2; else N -= 3;         // из-за нелинейности оценки
                    if (map.getValueAt(x + 1, y) == 1) E = 0;
                    if (map.getValueAt(x + 1, y) != 1 && map.getValueAt(x + 2, y) == 1 && map.getValueAt(x + 1, y - 1) == 1 && map.getValueAt(x + 1, y + 1) == 1)
                        if (E > 4) E /= 2; else E -= 3;         // из-за нелинейности оценки
                    if (map.getValueAt(x, y + 1) == 1) S = 0;
                    if (map.getValueAt(x, y + 1) != 1 && map.getValueAt(x, y + 2) == 1 && map.getValueAt(x - 1, y + 1) == 1 && map.getValueAt(x + 1, y + 1) == 1)
                        if (S > 4) S /= 2; else S -= 3;         // из-за нелинейности оценки
                    if (map.getValueAt(x - 1, y) == 1) W = 0;
                    if (map.getValueAt(x - 1, y) != 1 && map.getValueAt(x - 2, y) == 1 && map.getValueAt(x - 1, y - 1) == 1 && map.getValueAt(x - 1, y + 1) == 1)
                        if (W > 4) W /= 2; else W -= 3;         // из-за нелинейности оценки

                    //System.out.println("-----------------  Расчет тупика  -----------------");
                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за избежания тупика \n", N, E, S, W, direction);
                    }


                    if (b == 0 && a > 0) {
                        E++;
                    }
                    if (b == 0 && a < 0) {
                        W++;
                    }
                    if (a == 0 && b < 0) {
                        N++;
                    }
                    if (a == 0 && b > 0) {
                        S++;
                    }

                    if (b == 0 && a == 1) {
                        E *= 2;
                    }
                    if (b == 0 && a == -1) {
                        W *= 2;
                    }
                    if (a == 0 && b == 1) {
                        S *= 2;
                    }
                    if (a == 0 && b == -1) {
                        N *= 2;
                    }

                    if (a > 0) E++;
                    if (a < 0) W++;
                    if (b > 0) S++;
                    if (b < 0) N++;

                    //System.out.println("-----------------  За приближение  -----------------");
                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за приближения к цели \n", N, E, S, W, direction);
                    }

                    /*  Кедергіні айналып өту  */
                    if (direction == 0) {       // Up
                        if(map.getValueAt(x, y - 1) == 1) {
                            // target: x, y-2
                            if((map.getValueAt(x-1, y) != 1 && map.getValueAt(x-1, y - 1) != 1 && map.getValueAt(x-1, y -2) != 1 /*&& map.getValueAt(x, y - 2) != 1*/) ||
                               (map.getValueAt(x-1, y) != 1 && map.getValueAt(x-2, y) != 1 && map.getValueAt(x-2, y -1) != 1 && map.getValueAt(x-2, y - 2) != 1)) {
                                // сол жағымен айналып өтуге болады
                                W += 4;
                            }
                            if((map.getValueAt(x+1, y) != 1 && map.getValueAt(x+1, y - 1) != 1 && map.getValueAt(x+1, y -2) != 1 /*&& map.getValueAt(x, y - 2) != 1*/) ||
                               (map.getValueAt(x+1, y) != 1 && map.getValueAt(x+2, y) != 1 && map.getValueAt(x+2, y -1) != 1 && map.getValueAt(x+2, y - 2) != 1)){
                                // оң жағымен айналып өтуге болады
                                E += 4;
                            }
                        }
                    }
                    /*  Кедергіні айналып өту  */
                    if (direction == 1) {       // Right
                        if(map.getValueAt(x+1, y) == 1) {
                            // target: x+2, y
                            if((map.getValueAt(x, y-1) != 1 && map.getValueAt(x+1, y - 1) != 1 && map.getValueAt(x+2, y -1) != 1 /*&& map.getValueAt(x+2, y) != 1*/) ||
                               (map.getValueAt(x, y-1) != 1 && map.getValueAt(x, y - 2) != 1 && map.getValueAt(x+1, y -2) != 1 && map.getValueAt(x+2, y-2) != 1)) {
                                // сол жағымен айналып өтуге болады
                                N += 4;
                            }
                            if((map.getValueAt(x, y+1) != 1 && map.getValueAt(x+1, y + 1) != 1 && map.getValueAt(x+2, y +1) != 1 /*&& map.getValueAt(x+2, y) != 1*/) ||
                               (map.getValueAt(x, y+1) != 1 && map.getValueAt(x, y + 2) != 1 && map.getValueAt(x+1, y +2) != 1 && map.getValueAt(x+2, y+2) != 1)){
                                // оң жағымен айналып өтуге болады
                                S += 4;
                            }
                        }
                    }
                    if (direction == 2) {       // Down
                        if(map.getValueAt(x, y+1) == 1) {
                            // target: x, y+2
                            if((map.getValueAt(x+1, y) != 1 && map.getValueAt(x+1, y + 1) != 1 && map.getValueAt(x+1, y +2) != 1 /*&& map.getValueAt(x, y+2) != 1*/) ||
                               (map.getValueAt(x+1, y) != 1 && map.getValueAt(x+2, y) != 1 && map.getValueAt(x+2, y +1) != 1 && map.getValueAt(x+2, y+2) != 1)){
                                // сол жағымен айналып өтуге болады
                                E += 4;
                            }
                            if((map.getValueAt(x-1, y) != 1 && map.getValueAt(x-1, y + 1) != 1 && map.getValueAt(x-1, y +2) != 1 /*&& map.getValueAt(x, y+2) != 1*/) ||
                               (map.getValueAt(x-1, y) != 1 && map.getValueAt(x-2, y) != 1 && map.getValueAt(x-2, y +1) != 1 && map.getValueAt(x-2, y-2) != 1)){
                                // оң жағымен айналып өтуге болады
                                W += 4;
                            }
                        }
                    }
                    if (direction == 3) {       // Left
                        if(map.getValueAt(x-1, y) == 1) {
                            // target: x-2, y
                            if((map.getValueAt(x, y+1) != 1 && map.getValueAt(x-1, y + 1) != 1 && map.getValueAt(x-2, y +1) != 1 /*&& map.getValueAt(x-2, y) != 1*/) ||
                               (map.getValueAt(x, y+1) != 1 && map.getValueAt(x, y + 2) != 1 && map.getValueAt(x-1, y +2) != 1 && map.getValueAt(x-2, y+2) != 1)){
                                // сол жағымен айналып өтуге болады
                                S += 4;
                            }
                            if((map.getValueAt(x, y-1) != 1 && map.getValueAt(x-1, y - 1) != 1 && map.getValueAt(x-2, y -1) != 1 /*&& map.getValueAt(x-2, y) != 1*/) ||
                               (map.getValueAt(x, y-1) != 1 && map.getValueAt(x, y - 2) != 1 && map.getValueAt(x-1, y -2) != 1 && map.getValueAt(x-2, y-2) != 1)){
                                // оң жағымен айналып өтуге болады
                                N += 4;
                            }
                        }
                    }
                    if (debug) {
                        System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
                        System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за объезд препятствия \n", N, E, S, W, direction);
                    }

                    //System.out.printf("N = %d, E = %d, S = %d, W = %d%n", N, E, S, W);
                    if (debug)
                        System.out.println("---------------------------------------------------------------------------------");

//                if (N >= E && N >= S && N >= W && y != 0) {
////                    System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y - 1);
////                    System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
////                    direction = 0;
//                    if( x>=0 && x<size && y-1>=0 && y-1<size ) {
//                        if(m[y-1][x]==1) {
//                            //N -= 2;
//                            System.out.println("N");
//                        }
//                    }
//                } else
//                if (E >= N && E >= S && E >= W && x != size) {
////                    System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x + 1, y);
////                    System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
////                    direction = 1;
//                    if(x+1>=0 && x+1<size && y>=0 && y<size) {
//                        if(m[y][x+1]==1) {
//                            //E -= 2;
//                            System.out.println("E");
//                        }
//                    }
//                } else
//                if (S >= N && S >= E && S >= W && y != size) {
////                    System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y + 1);
////                    System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
////                    direction = 2;
//                    if(x>=0 && x<size && y+1>=0 && y+1<size) {
//                        if(m[y+1][x]==1) {
//                            //S -= 2;
//                            System.out.println("S");
//                        }
//                    }
//                } else
//                if (W >= N && W >= E && W >= S && x != 0) {
////                    System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x - 1, y);
////                    System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d%n", N, E, S, W, direction);
////                    direction = 3;
//                    if(x-1>=0 && x-1<size && y>=0 && y<size) {
//                        if(m[y][x-1]==1) {
//                            //W -= 2;
//                            System.out.println("W");
//                        }
//                    }
//                }


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


//                System.out.printf("a = %d, b = %d, (%d, %d) \t", a, b, x, y);
//                System.out.printf("N = %d, E = %d, S = %d, W = %d, direction = %d  <---------- за повторения траектория \n", N, E, S, W, direction);

                    //***************************************************************************************

                    //int d = rating(direction);
                    int d = direction;

                    switch (d) {
                        case 0:
                            //m[y-1][x] = 1;
                            //Platform.runLater(() -> {
                            moveUp();
//                            System.out.println("Up: " + d);
                            //});
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            //m[y][x+1] = 1;
                            //Platform.runLater(() -> {
                            moveRight();
//                            System.out.println("Right: " + d);
                            //});
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            //m[y+1][x] = 1;
                            //Platform.runLater(() -> {
                            moveDown();
//                            System.out.println("Down: " + d);
                            //});
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            //m[y][x-1] = 1;
                            //Platform.runLater(() -> {
                            moveLeft();
//                            System.out.println("Left: " + d);
                            //});
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            System.out.println(d);
                    }
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }


//                } while (false);
                } while (true && !debug);

//            while (!getPosition().equals(food.getPosition())) {
//                /*  бірінші бағытты анықтап алу керек
//                 *  food пен bot-тың координаттарын салыстыру керек
//                 */
//                Long delay = 10L;
//                Boolean downValid = true;
//                Boolean upValid = true;
//                Boolean rightValid = true;
//                Boolean leftValid = true;
//
//                int cycle = food.getPosition().getY() - this.getPosition().getY();
//                if (cycle > 0) {
//                    for (; cycle > 0; cycle--) {
//                        // төмен қарай жүреміз
//                        /* кедергіні айналып өтуді ойластыру керек
//                         * 1. төмен қарай түсеміз деп есептейміз
//                         * 2. 3 одан кейінгі бағытын тексереміз
//                         * 3. егер таза болса түсеміз
//                         *      таза болмаса не істейміз
//                         */
//                        /* қарапайым түрі:
//                         *  бір ғана клетканы тексереміз
//                         */
//                        //System.out.println((map.getValueAt(position.getX(),position.getY()+1)) + "," +
//                        //        (map.getValueAt(position.getX()-1,position.getY()+1)) + "," +
//                        //        (map.getValueAt(position.getX()+1,position.getY()+1)));
//
//                        if (map.getValueAt(position.getX(), position.getY() + 1) == 0 &&
//                                (map.getValueAt(position.getX() - 1, position.getY() + 1) == 0 ||
//                                        (map.getValueAt(position.getX() + 1, position.getY() + 1) == 0) ||
//                                        (map.getValueAt(position.getX(), position.getY() + 2)) == 0)) {
//                            Platform.runLater(() -> {
//                                this.moveDown();
//                                System.out.println("Төмен down");
//                            });
//                        } else if (map.getValueAt(position.getX() - 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveLeft();
//                                System.out.println("Төмен left");
//                            });
//                        } else if (map.getValueAt(position.getX() + 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveRight();
//                                System.out.println("Төмен right");
//                            });
//                        } else {
//                            downValid = false;
//                            System.out.println("Төмен break");
//                            break;
//                        }
//                        try {
//                            Thread.sleep(delay);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                cycle = food.getPosition().getY() - this.getPosition().getY();
//                if (cycle < 0) {
//                    for (; cycle < 0; cycle++) {
//                        // жоғары қарай жүреміз
//                        if (map.getValueAt(position.getX(), position.getY() - 1) == 0 &&
//                                (map.getValueAt(position.getX() - 1, position.getY() - 1) == 0 ||
//                                        (map.getValueAt(position.getX() + 1, position.getY() - 1) == 0) ||
//                                        (map.getValueAt(position.getX(), position.getY() - 2)) == 0)) {
//                            Platform.runLater(() -> {
//                                this.moveUp();
//                                System.out.println("Жоғары up");
//                            });
//                        } else if (map.getValueAt(position.getX() - 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveLeft();
//                                System.out.println("Жоғары left");
//                            });
//                        } else if (map.getValueAt(position.getX() + 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveRight();
//                                System.out.println("Жоғары right");
//                            });
//                        } else {
//                            upValid = false;
//                            System.out.println("Жоғары break");
//                            break;
//                        }
//                        try {
//                            Thread.sleep(delay);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                cycle = food.getPosition().getX() - this.getPosition().getX();
//                if (cycle > 0) {
//                    for (; cycle > 0; cycle--) {
//                        // оңға қарай жүреміз
//                        if (map.getValueAt(position.getX() + 1, position.getY()) == 0 &&
//                                (map.getValueAt(position.getX() + 1, position.getY() - 1) == 0 ||
//                                        (map.getValueAt(position.getX() + 1, position.getY() + 1) == 0) ||
//                                        (map.getValueAt(position.getX() + 2, position.getY())) == 0)) {
//                            Platform.runLater(() -> {
//                                this.moveRight();
//                                System.out.println("Оңға right");
//                            });
//                        } else if (map.getValueAt(position.getX() + 1, position.getY() - 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveUp();
//                                System.out.println("Оңға up");
//                            });
//                        } else if (map.getValueAt(position.getX() + 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveDown();
//                                System.out.println("Оңға down");
//                            });
//                        } else {
//                            rightValid = false;
//                            System.out.println("Оңға break");
//                            break;
//                        }
//                        try {
//                            Thread.sleep(delay);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                cycle = food.getPosition().getY() - this.getPosition().getY();
//                if (cycle < 0) {
//                    for (; cycle < 0; cycle++) {
//                        // солға қарай жүреміз
//                        if (map.getValueAt(position.getX() - 1, position.getY()) == 0 &&
//                                (map.getValueAt(position.getX() - 1, position.getY() - 1) == 0 ||
//                                        (map.getValueAt(position.getX() - 1, position.getY() + 1) == 0) ||
//                                        (map.getValueAt(position.getX() - 2, position.getY())) == 0)) {
//                            Platform.runLater(() -> {
//                                this.moveLeft();
//                                System.out.println("Солға left");
//                            });
//                        } else if (map.getValueAt(position.getX() - 1, position.getY() - 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveUp();
//                                System.out.println("Солға up");
//                            });
//                        } else if (map.getValueAt(position.getX() - 1, position.getY() + 1) == 0) {
//                            Platform.runLater(() -> {
//                                this.moveDown();
//                                System.out.println("Солға down");
//                            });
//                        } else {
//                            leftValid = false;
//                            System.out.println("Солға break");
//                            break;
//                        }
//                        try {
//                            Thread.sleep(delay);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                /* food табылды, енді 10 мс келесі тамақты күтеміз */
//                try {
//                    Thread.sleep(1000L);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
                try {
                    Thread.sleep(1050);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(count);
            }
        });
        eating_bot.start();
    }

    @Override
    public synchronized void moveRight() {
        int x = position.getX();
        int y = position.getY();
        int size = map.getSize();

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
        int size = map.getSize();

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
