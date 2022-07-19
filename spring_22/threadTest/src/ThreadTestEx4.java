public class ThreadTestEx4 {
    public static void main(String[] args) {
        ThreadEx1Sleep th1 = new ThreadEx1Sleep();
        ThreadEx2Sleep th2 = new ThreadEx2Sleep();
        th1.start();
        th2.start();

        try{
            th1.sleep(2000);
            // th1을 2초간 일시정지 시켰으므로 th1이 가장 늦게 종료될 것으로 예상하였으나.
            // sleep은 항상 지금 실행 중인 쓰레드에 대해서 동작하기 때문에 main이 2초간 일시정지 된다.
            // sleep은 static메소드 이므로 참조변수를 이용한 호출은 지양하고 Thread.sleep과 같은 방식을 이용해야 한다.
        }catch (InterruptedException e){}
        System.out.print("<< main 종료 >>");
    }
}
class ThreadEx1Sleep extends Thread{
    @Override
    public void run() {
        for (int i = 0;i < 300;i++){
            System.out.print("-");
        }
        System.out.print("<< th1 종료 >>");
    }
}

class ThreadEx2Sleep extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            System.out.print("|");
        }
        System.out.print("<< th2 종료 >>");
    }
}
