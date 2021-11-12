package pl.emlo.LotrShima.LotrShima.model.entity;

import lombok.Builder;
import lombok.Data;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
public class Backpack {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @Builder.Default
    private List<Item> items = new LinkedList<>();

    private double cash;

    public List<Item> getItemsByItemType(ItemType itemType) {
        return items.stream().filter(item -> item.getType().equals(itemType)).collect(Collectors.toList());
    }

    public int getTotalWeight() {
        return items.stream().mapToInt(Item::calculateTotalWeight).sum();
    }
    void addItem(Item addedItem){
        Item actualItem = items.stream().filter(item -> item.equals(addedItem)).findAny().orElse(null);
        if(actualItem != null){
            actualItem.setQuantity(actualItem.getQuantity()+ addedItem.getQuantity());
        }
        else{
            items.add(addedItem);
        }
    }
}
