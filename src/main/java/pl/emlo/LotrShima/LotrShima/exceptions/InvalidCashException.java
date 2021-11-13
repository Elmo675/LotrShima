package pl.emlo.LotrShima.LotrShima.exceptions;

public class InvalidCashException extends Exception{
    public InvalidCashException(){
        super("THERE IS NOT ENOUGH MONEY");
    }
}
