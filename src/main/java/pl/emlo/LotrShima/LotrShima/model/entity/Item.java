package pl.emlo.LotrShima.LotrShima.model.entity;

import lombok.*;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    private int[] powerOfItem;

    private int armorDefense;

    @Builder.Default
    private int quantity = 1;

    private int weight;

    private double prize;

    public int calculateTotalWeight() {
        return weight * quantity;
    }

    public double calculateTotalPrize() {
        return prize * quantity;
    }

    int getAttackPower(int turns) {
        if (turns <= 0 || turns > 3) {
            return -1;
        }
        return powerOfItem[turns];
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", powerOfItem=" + Arrays.toString(powerOfItem) +
                ", armorDefense=" + armorDefense +
                ", quantity=" + quantity +
                ", weight=" + weight +
                ", prize=" + prize +
                '}';
    }
}
