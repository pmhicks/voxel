import java.awt.EventQueue;

public class SwingAnimator implements Runnable {
    public volatile int fps = -1;
    
    private Thread thread;
    private Runnable renderer;
    private long delay;
    
    public SwingAnimator(Runnable r, long delayInMs)  {
        this.renderer = r;
        this.delay = delayInMs;
        this.thread = new Thread(this);
    }
    
    public void start() {
        thread.start();
    }

    
    @Override
    public void run() {
        
        long before, sleep;
        double diff, lastTime = 0;
        
        while (true) {
            before = System.currentTimeMillis();
            try {
                EventQueue.invokeAndWait(renderer);
            } catch (Exception e) {
                System.err.println("Animator Invoke Exception: " + e);
            }
            sleep = delay - (System.currentTimeMillis() - before);
            
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    //dont care if interrupted
                }
            }
            
            diff = (System.currentTimeMillis() - before);
            diff = .9 * diff + .1 * lastTime;
            lastTime = diff;
            fps = (int) ( 1 / diff * 1000);
            
        }
    }

}
