package pl.emlo.LotrShima.LotrShima.model.entity;

import lombok.*;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidBodyPartsCoveredInPercentageException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidBodyTypeException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidTurnsException;
import pl.emlo.LotrShima.LotrShima.model.enums.BodyType;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemType type = ItemType.OTHERS;

    @ElementCollection
    private List<Integer> powerOfItem;

    @Enumerated
    private BodyType heldIn;
    @ElementCollection
//    @CollectionTable(name = "bodyPartsCoveredInPercentage",
//            joinColumns = {@JoinColumn(name = "item_id", referencedColumnName = "id")})
//    @MapKeyEnumerated
//    @Column(name = "percentage")
    private Map<BodyType, Integer> bodyPartsCoveredInPercentage;

    @Builder.Default
    private int quantity = 1;

    @Builder.Default
    private int weight = 0;

    private double prize;


    public double calculateTotalPrize() {
        return prize * quantity;
    }

    public int calculateTotalHealingPower() {
        return getHealingPower() * quantity;
    }

    public int getHealingPower() {
        if (type != ItemType.MEDICINE) {
            return 0;
        }
        return getPower(0);
    }

    private int getPower(int index) {
        if (powerOfItem == null || index >= powerOfItem.size() || index < 0) {
            return 0;
        }
        return powerOfItem.get(index);
    }

    public int getAttackPower(int turns) throws InvalidTurnsException {
        if (type != ItemType.WEAPON) {
            return 0;
        }
        if (turns <= 0 || turns >= 4) {
            throw new InvalidTurnsException("Attack turns should be between 1 and 3, but is: " + turns);
        }
        return getPower(turns - 1);
    }

    public int getDefensePowerForShieldWhenBlocking(BodyType whereAttacked, int percentage) throws InvalidBodyPartsCoveredInPercentageException {
        percentage = percentage == 0 ? 0 : percentage / 2;
        return getDefensePower(whereAttacked, percentage);
    }

    public int getDefensePower(BodyType whereAttacked, int percentage) throws InvalidBodyPartsCoveredInPercentageException {
        int returned;
        if (type != ItemType.ARMOR && type != ItemType.SHIELD) {
            returned = 0;
        } else if (isBodyPartsCoveredInPercentageHasNotAllFieldsInitialized()) {
            throw new InvalidBodyPartsCoveredInPercentageException("There is wrongly assigned map for an Armor Body parts covered in percentage field");
        } else {
            if (determineIfArmorWillDefend(whereAttacked, percentage)) {
                returned = getPower(0);
            } else {
                returned = 0;
            }
        }
        return returned;
    }

    private boolean isBodyPartsCoveredInPercentageHasNotAllFieldsInitialized() {
        if (bodyPartsCoveredInPercentage == null) {
            return true;
        }
        for (BodyType type : BodyType.values()
        ) {
            if (!bodyPartsCoveredInPercentage.containsKey(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean determineIfArmorWillDefend(BodyType whereAttacked, int percentage) {
        if (heldIn == null) {
            return false;
        }
        return bodyPartsCoveredInPercentage.get(whereAttacked) > percentage;
    }


    private void swapForShieldHoldingInOtherHand() {
        int rightArm = bodyPartsCoveredInPercentage.get(BodyType.RIGHT_ARM);
        int rightLeg = bodyPartsCoveredInPercentage.get(BodyType.RIGHT_LEG);
        int leftArm = bodyPartsCoveredInPercentage.get(BodyType.LEFT_ARM);
        int leftLeg = bodyPartsCoveredInPercentage.get(BodyType.LEFT_LEG);
        bodyPartsCoveredInPercentage.put(BodyType.RIGHT_ARM, leftArm);
        bodyPartsCoveredInPercentage.put(BodyType.RIGHT_LEG, leftLeg);
        bodyPartsCoveredInPercentage.put(BodyType.LEFT_ARM, rightArm);
        bodyPartsCoveredInPercentage.put(BodyType.LEFT_LEG, rightLeg);
    }

    public void setHeldIn(BodyType heldIn) throws InvalidBodyPartsCoveredInPercentageException, InvalidBodyTypeException {
        switch (type) {
            case ARMOR:
                if (isBodyPartsCoveredInPercentageHasNotAllFieldsInitialized()) {
                    throw new InvalidBodyPartsCoveredInPercentageException("There is wrongly assigned map for an Armor Body parts covered in percentage field");
                }
                this.heldIn = heldIn;
                break;
            case SHIELD:
                if (isBodyPartsCoveredInPercentageHasNotAllFieldsInitialized()) {
                    throw new InvalidBodyPartsCoveredInPercentageException("There is wrongly assigned map for an Armor Body parts covered in percentage field");
                }
                if (heldIn == BodyType.LEFT_ARM) {
                    if (bodyPartsCoveredInPercentage.get(BodyType.LEFT_ARM) < bodyPartsCoveredInPercentage.get(BodyType.RIGHT_ARM)) {
                        swapForShieldHoldingInOtherHand();
                    }
                } else if (heldIn == BodyType.RIGHT_ARM) {
                    if (bodyPartsCoveredInPercentage.get(BodyType.RIGHT_ARM) < bodyPartsCoveredInPercentage.get(BodyType.LEFT_ARM)) {
                        swapForShieldHoldingInOtherHand();
                    }
                }
            case WEAPON:
                if (heldIn != null && heldIn != BodyType.LEFT_ARM && heldIn != BodyType.RIGHT_ARM)
                    throw new InvalidBodyTypeException("Weapons and Shields should be held in hands!!!");
            default:
                this.heldIn = heldIn;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
