public class ThreadTestEx6 {
    public static void main(String[] args) {
        Thread1Join th1 = new Thread1Join();
        th1.setDaemon(true);
        th1.start();

        int requiredMemory = 0;
        for(int i = 0;i < 20;i++){
            requiredMemory = (int) (Math.random() * 10) * 20;
            if(th1.freeMemory() < requiredMemory || th1.freeMemory() < th1.totalMemory() * 0.4){ // 메모리가 40% 미만인 경우 interrupt 발생
                th1.interrupt(); // th1을 깨워 즉시 gc수행
                // interrput만 실행 하면 main도 같이 돌아가고 있으므로 결국 1000을 넘기게 된다.
                // 이를 방지하고자 join을 사용하여 gc가 작업할 시간을 어느정도 주고 실행 하도록 함.
                try{
                    th1.join(100);
                }catch (InterruptedException e){}
            }
            th1.usedMemory += requiredMemory;
            System.out.println("usedMemory: " + th1.usedMemory);
        }
    }
}

class Thread1Join extends Thread{
    // 10초 마다 가비지컬렉션을 자동 실행
    final static int MAX_MEMORY = 1000;
    int usedMemory = 0;
    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(10 * 1000);
            }catch (InterruptedException e){
                System.out.println("Awaken by Interrupt().");
            }

            gc();
            System.out.println("Garbage Clooected. Free Memeory: " + freeMemory());
        }

    }

    public void gc() {
        usedMemory -= 300;
        if(usedMemory < 0)
            usedMemory = 0;
    }
    public int totalMemory(){
        return MAX_MEMORY;
    }
    public int freeMemory(){
        return MAX_MEMORY - usedMemory;
    }
}
