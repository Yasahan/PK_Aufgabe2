public class TrafficControllerSimple implements TrafficController{

    private TrafficRegistrar registrar;
    private boolean bridgeFree;

    public TrafficControllerSimple(TrafficRegistrar registrar) {
        this.registrar = registrar;
        this.bridgeFree = true;
    }

    @Override
    public synchronized void enterRight(Vehicle v) {

        while(!bridgeFree){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        registrar.registerRight(v);
        this.bridgeFree = false;
    }

    @Override
    public synchronized void enterLeft(Vehicle v) {

        while(!bridgeFree){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        registrar.registerLeft(v);
        this.bridgeFree = false;

    }

    @Override
    public synchronized void leaveLeft(Vehicle v) {
        this.bridgeFree = true;
        registrar.deregisterLeft(v);
        notify();
    }

    @Override
    public synchronized void leaveRight(Vehicle v) {
        this.bridgeFree = true;
        registrar.deregisterRight(v);
        notify();
    }
}
