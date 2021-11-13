package pl.emlo.LotrShima.LotrShima.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
public class Backpack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    private double cash;


    public void addItem(Item addedItem) {
        Item actualItem = items.stream().filter(item -> item.equals(addedItem)).findAny().orElse(null);
        if (actualItem != null) {
            actualItem.setQuantity(actualItem.getQuantity() + addedItem.getQuantity());
        } else {
            items.add(addedItem);
        }
    }
}
