package pl.emlo.LotrShima.LotrShima.utils;

import pl.emlo.LotrShima.LotrShima.model.entity.Item;
import pl.emlo.LotrShima.LotrShima.model.enums.BodyType;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    private static final List<Integer> DRAGON_SWORD_DAMAGE = new ArrayList<>(List.of(1,3,4));
    private static final List<Integer> HEALING_HERB_POWER = new ArrayList<>(List.of(1));
    private static final List<Integer> SHIELD_DEFENSE_POWER = new ArrayList<>(List.of(5));
    private static final List<Integer> ARMOR_DEFENSE_POWER = new ArrayList<>(List.of(3));
    private static final Map<BodyType, Integer> bodyPartsCoveredInPercentage = generateArmorBodyPartsCoveredInPercentageForShieldItem();

    public static Item getWeaponItem() {
        return Item.builder()
                .name("Dragon Sword")
                .type(ItemType.WEAPON)
                .powerOfItem(DRAGON_SWORD_DAMAGE)
                .heldIn(BodyType.RIGHT_ARM)
                .weight(1500).prize(4000.0)
                .build();
    }

    public static Item getShieldItem() {
        return Item.builder()
                .name("Dragon Shield")
                .type(ItemType.SHIELD)
                .bodyPartsCoveredInPercentage(bodyPartsCoveredInPercentage)
                .powerOfItem(SHIELD_DEFENSE_POWER)
                .weight(1500)
                .prize(2000.0)
                .build();
    }

    public static Item getArmorItem() {
        return Item.builder()
                .name("Dragon Armor")
                .type(ItemType.ARMOR)
                .bodyPartsCoveredInPercentage(bodyPartsCoveredInPercentage)
                .powerOfItem(ARMOR_DEFENSE_POWER)
                .heldIn(BodyType.TORSO)
                .weight(7000)
                .prize(8000.0)
                .build();
    }

    public static Item getApplesItem() {
        return Item.builder()
                .name("Green Apple")
                .type(ItemType.FOOD)
                .quantity(100)
                .weight(200)
                .prize(5.32)
                .build();
    }

    public static Item getHealingItem() {
        return Item.builder()
                .name("healing herb")
                .type(ItemType.MEDICINE)
                .powerOfItem(HEALING_HERB_POWER)
                .quantity(50)
                .weight(1)
                .prize(0.32)
                .build();
    }

    private static HashMap<BodyType, Integer> generateArmorBodyPartsCoveredInPercentageForShieldItem() {
        HashMap<BodyType, Integer> armorBodyPartsCoveredInPercentage = new HashMap<>();
        armorBodyPartsCoveredInPercentage.put(BodyType.HEAD, 5);
        armorBodyPartsCoveredInPercentage.put(BodyType.TORSO, 40);
        armorBodyPartsCoveredInPercentage.put(BodyType.LEFT_ARM, 30);
        armorBodyPartsCoveredInPercentage.put(BodyType.RIGHT_ARM, 15);
        armorBodyPartsCoveredInPercentage.put(BodyType.LEFT_LEG, 15);
        armorBodyPartsCoveredInPercentage.put(BodyType.RIGHT_LEG, 8);
        return armorBodyPartsCoveredInPercentage;
    }
}
