import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficControllerFair implements TrafficController {

    private final TrafficRegistrar registrar;
    private boolean bridgeFree;
    private boolean fair;

    private Lock lock;
    private Condition cond;

    TrafficControllerFair(TrafficRegistrar registrar) {
        this.registrar = registrar;
        this.bridgeFree = true;
        this.fair = true;
        lock = new ReentrantLock(fair);
        cond = lock.newCondition();
    }


    @Override
    public void enterRight(Vehicle v) {
        lock.lock();
        while(!bridgeFree){
            try {
                cond.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        registrar.registerRight(v);
        this.bridgeFree = false;
    }

    @Override
    public void enterLeft(Vehicle v) {
        lock.lock();
        while(!bridgeFree){
            try {
                cond.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        registrar.registerLeft(v);
        this.bridgeFree = false;
    }

    @Override
    public void leaveLeft(Vehicle v) {
        this.bridgeFree = true;
        registrar.deregisterLeft(v);
        lock.unlock();
    }

    @Override
    public void leaveRight(Vehicle v) {
        this.bridgeFree = true;
        registrar.deregisterRight(v);
        lock.unlock();
    }
}
