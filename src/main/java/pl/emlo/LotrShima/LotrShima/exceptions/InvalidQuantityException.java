package pl.emlo.LotrShima.LotrShima.exceptions;

public class InvalidQuantityException extends Exception{
    public InvalidQuantityException(String itemName){
        super("THERE IS NOT ENOUGH QUANTITY OF ITEM: "+ itemName);
    }
}
