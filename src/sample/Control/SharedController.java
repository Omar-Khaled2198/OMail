package sample.Control;


public abstract class SharedController {

    private Access access;

    public SharedController(Access access) {
        this.access = access;
    }

    public Access getAccess() {
        return access;
    }

}
