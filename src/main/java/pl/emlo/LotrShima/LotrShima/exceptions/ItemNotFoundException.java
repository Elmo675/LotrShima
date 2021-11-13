package pl.emlo.LotrShima.LotrShima.exceptions;

public class ItemNotFoundException extends Exception{
    public ItemNotFoundException(){
        super("THAT ITEM CAN NOT BE FOUND");
    }
    public ItemNotFoundException(Long id){
        super("ITEM WITH GIVEN ID: "+id+" DOES NOT EXIST");
    }
    public ItemNotFoundException(String name){
        super("ITEM: "+name +" DOES NOT EXIST");
    }
}
