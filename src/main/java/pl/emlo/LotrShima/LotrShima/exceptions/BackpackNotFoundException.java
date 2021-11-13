package pl.emlo.LotrShima.LotrShima.exceptions;

public class BackpackNotFoundException extends Exception {
    public BackpackNotFoundException(Long id) {
        super("Backpack with given id: " + id + " does not exists");
    }
}
