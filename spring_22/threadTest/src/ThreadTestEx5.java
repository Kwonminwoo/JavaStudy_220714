import javax.swing.*;

public class ThreadTestEx5 {
    public static void main(String[] args) {
        ThreadEx1Interrupt th1 = new ThreadEx1Interrupt();
        th1.start();
        System.out.println("isInterrupted(): " + th1.isInterrupted());

        String input = JOptionPane.showInputDialog("입력하세요");
        System.out.println("input = " + input);
        th1.interrupt(); // 쓰레드 작업 중지 요청
        System.out.println("isInterrupted(): " + th1.isInterrupted());
    }
}

class ThreadEx1Interrupt extends  Thread{
    @Override
    public void run() {
        int i = 10;
        while(i != 0 && !isInterrupted()){
            System.out.println(i--);
            for(long x = 0;x < 2500000000L;x++); // 시간 지연용
        }
        System.out.println("카운트가 종료 됨");
    }
}
