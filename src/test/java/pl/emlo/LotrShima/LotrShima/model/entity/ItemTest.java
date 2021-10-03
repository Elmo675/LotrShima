package pl.emlo.LotrShima.LotrShima.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {
    private Item weaponItem;
    private Item shieldItem;
    private Item applesItem;

    @BeforeEach
    void setUp() {
        weaponItem = Item.builder()
                .name("Dragon Sword")
                .type(ItemType.WEAPON)
                .powerOfItem(new int[]{1, 3, 4})
                .weight(1500).prize(4000.0)
                .build();

        shieldItem = Item.builder()
                .name("Dragon Shield")
                .type(ItemType.SHIELD)
                .armorDefense(5)
                .weight(1500).prize(2000.0)
                .build();

        applesItem = Item.builder()
                .name("Green Apple")
                .type(ItemType.FOOD)
                .quantity(100)
                .weight(200)
                .prize(5.32)
                .build();
    }

    @Test
    void givenWeaponItem_WhenCalculateTotalWeight_ThenCalculatedProperly() {
        //given from setUp
        int expectedWeight = 1500;
        //when
        int actualWeight = weaponItem.calculateTotalWeight();
        //then
        assertThat(actualWeight).isEqualTo(expectedWeight);
    }

    @Test
    void givenApplesItem_WhenCalculateTotalWeight_ThenCalculatedProperly() {
        //given from setUp
        int expectedWeight = 200 * 100; // there is 100 apples with 200grams per each
        //when
        int actualWeight = applesItem.calculateTotalWeight();
        //then
        assertThat(actualWeight).isEqualTo(expectedWeight);
    }

    @Test
    void givenWeaponItem_WhenCalculateTotalPrize_ThenCalculatedProperly() {
        //given from setUp
        double expectedPrize = 4000.0;
        //when
        double actualPrize = weaponItem.calculateTotalPrize();
        //then
        assertThat(actualPrize).isEqualTo(expectedPrize);
    }

    @Test
    void givenApplesItem_WhenCalculateTotalPrize_ThenCalculatedProperly() {
        //given from setUp
        double expectedPrize = 5.32 * 100; // there is 100 apples with 200grams per each
        //when
        double actualPrize = applesItem.calculateTotalPrize();
        //then
        assertThat(actualPrize).isEqualTo(expectedPrize);
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerForOneTurn_ThenGetThreeValue() {
        //given from setUp
        int expectedAttack = 3;
        //when
        int actualAttack = weaponItem.getAttackPower(1);
        //then
        assertThat(actualAttack).isEqualTo(expectedAttack);
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerWithMinusValue_ThenReturnMinusOne() {
        //given from setUp
        int expectedAttack = -1; //minus value means we have some error here
        //when
        int actualAttack = weaponItem.getAttackPower(-1); //turn is -1 means invalid
        //then
        assertThat(actualAttack).isEqualTo(expectedAttack);
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerWithMoreThanThreeValue_ThenReturnMinusOne() {
        //given from setUp
        int expectedAttack = -1; //minus value means we have some error here
        //when
        int actualAttack = weaponItem.getAttackPower(5); //turn is 5 means invalid
        //then
        assertThat(actualAttack).isEqualTo(expectedAttack);
    }
}