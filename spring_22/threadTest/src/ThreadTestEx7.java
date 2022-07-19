import java.util.ArrayList;

public class ThreadTestEx7 {
    public static void main(String[] args) {
        Table table = new Table();

        new Thread(new Cook(table), "COOK1").start();
        new Thread(new Customer(table, "donut"), "CUST1").start();
        new Thread(new Customer(table, "burger"), "CUST2").start();

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){}
        System.exit(0);
    }
}

class Customer implements Runnable{
    private Table table;
    private String food;

    public Customer(Table table, String food) {
        this.table = table;
        this.food = food;
    }

    @Override
    public void run() {
        while (true){
            try{
                Thread.sleep(10);
            }catch (InterruptedException e){}
            String name = Thread.currentThread().getName();
            table.remove(food);
            System.out.println(name + " ate a " + food);
        }
    }
}

class Cook implements Runnable{
    private Table table;
    Cook(Table table){
        this.table = table;
    }
    @Override
    public void run() {
        while(true){
            int idx = (int) (Math.random() * table.dishNum());
            table.add(table.dishNames[idx]);

            try{
                Thread.sleep(1);
            }catch (InterruptedException e){}
        }
    }
}

class Table {
    String [] dishNames = {"donut", "donut", "burger"};
    final int MAX_FOOD = 6; // 테이블에 놓을 수 있는 최대 개수
    private ArrayList<String> dishes = new ArrayList<>(); // 테이블에 놓여진 요리


    // 테이블에 추가
    public synchronized void add(String dish){ // 동기화
        while(dishes.size() >= MAX_FOOD){
            String name = Thread.currentThread().getName();
            System.out.println(name + " is waiting");
            try {
                wait(); // 테이블에 음식이 가득 찬 경우 COOK 쓰레드가 기다리게 함.
                Thread.sleep(500);
            }catch (InterruptedException e){}
        }
        dishes.add(dish);
        notify(); // Customer 쓰레드에게 요리가 나왔음을 통지함
        System.out.println("Dishes: " + dishes.toString());
    }

    // 테이블에서 소비
    public void remove (String dishName){
        synchronized (this){ // 동기화 (블록으로 하나 안 하나 똑같음)
            String name = Thread.currentThread().getName();
            while(dishes.size() == 0){
                System.out.println(name + " is waiting");
                try{
                    wait(); // 테이블에 요리가 하나도 없는 경우 Customer 쓰레드가 기다리게 함.
                    Thread.sleep(500);
                }catch (InterruptedException e){}
            }
            while (true){
                for(int i = 0;i < dishes.size();i++){
                    if(dishName.equals(dishes.get(i))){
                        dishes.remove(i);
                        notify(); // COOK 쓰레드에게 테이블에 빈 공간이 생겼다고 알림
                        return;
                    }
                }

                try{
                    System.out.println(name + " is waiting");
                    wait(); // 원하는 음식이 없는 Customer를 기다리게 함
                    Thread.sleep(500);
                }catch (InterruptedException e) {}
            }
        }
    }
    public int dishNum(){
        return dishNames.length;
    }
}
