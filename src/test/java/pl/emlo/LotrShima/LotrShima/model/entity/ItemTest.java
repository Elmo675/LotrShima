package pl.emlo.LotrShima.LotrShima.model.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidBodyPartsCoveredInPercentageException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidBodyTypeException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidTurnsException;
import pl.emlo.LotrShima.LotrShima.model.enums.BodyType;
import pl.emlo.LotrShima.LotrShima.utils.ItemUtils;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {
    private Item weaponItem;
    private Item shieldItem;
    private Item applesItem;
    private Item armorItem;
    private Item healingItem;

    @BeforeEach
    void setUp() {
        weaponItem = ItemUtils.getWeaponItem();
        shieldItem = ItemUtils.getShieldItem();
        armorItem = ItemUtils.getArmorItem();
        applesItem = ItemUtils.getApplesItem();
        healingItem = ItemUtils.getHealingItem();
    }

    @Test
    void givenHealingItem_WhenCalculateTotalHealingPower_ThenCalculateHealingPowerProperly() {
        //given from setUp
        int expectedHealingPower = 50;
        //when
        int actualHealingPower = healingItem.calculateTotalHealingPower();
        //then
        assertThat(actualHealingPower).isEqualTo(expectedHealingPower);
    }

    @Test
    void givenShieldItem_WhenGetHealingPower_ThenReturnZero() {
        //given from setUp
        //when
        int actualHealingPower = shieldItem.getHealingPower();
        //then
        assertThat(actualHealingPower).isZero();
    }

    @Test
    void givenHealingItemWithNoPower_WhenGetHealingPower_ThenReturnZero() {
        //given from setUp
        healingItem.setPowerOfItem(new LinkedList<>());
        //when
        int actualHealingPower = healingItem.getHealingPower();
        //then
        assertThat(actualHealingPower).isZero();
    }

    @Test
    void givenArmorItem_WhenGetDefensePowerWithSmallerChance_ThenReturnArmorDefense() {
        //given from setUp
        int expectedDefensePower = armorItem.getPowerOfItem().get(0);
        final int[] actualDefensePower = new int[1];
        Assertions.assertAll(
                //when
                () -> actualDefensePower[0] = armorItem.getDefensePower(BodyType.TORSO, 1),
                //then
                () -> assertThat(actualDefensePower[0]).isEqualTo(expectedDefensePower)
        );
    }

    @Test
    void givenArmorItem_WhenGetDefensePowerWithBiggerChance_ThenReturnZero() {
        //given from setUp
        final int[] actualDefensePower = new int[1];
        //Torso has 40 chance to defend, so 99 percentege should means that we do not defend
        Assertions.assertAll(
                //when
                () -> actualDefensePower[0] = armorItem.getDefensePower(BodyType.TORSO, 99),
                //then
                () -> assertThat(actualDefensePower[0]).isZero()
        );
    }

    @Test
    void givenArmorItemNotHeld_WhenGetDefensePowerWithSmallerChance_ThenReturnZero() throws InvalidBodyPartsCoveredInPercentageException, InvalidBodyTypeException {
        //given
        final int[] actualDefensePower = new int[1];
        armorItem = ItemUtils.getArmorItem();
        armorItem.setHeldIn(null);


        Assertions.assertAll(
                //when
                () -> actualDefensePower[0] = armorItem.getDefensePower(BodyType.TORSO, 1),
                //then
                () -> assertThat(actualDefensePower[0]).isZero()
        );

    }

    @Test
    void givenWeaponItem_WhenGetDefensePowerWithSmallerChance_ThenReturnZero() {
        //given from setUp
        final int[] actualDefensePower = new int[1];
        Assertions.assertAll(
                //when
                () -> actualDefensePower[0] = weaponItem.getDefensePower(BodyType.HEAD, 1),
                //then
                () -> assertThat(actualDefensePower[0]).isZero()
        );
    }

    @Test
    void givenArmorItemWithBadlySetArmorBodyPartsCoveredInPercentageField_WhenGetDefensePower_ThenThrowInvalidArmorBodyPartsCoveredInPercentageException() {
        //given
        armorItem.setBodyPartsCoveredInPercentage(null);
        //when
        //then
        assertThatThrownBy(() -> armorItem.getDefensePower(BodyType.HEAD, 2)).isInstanceOf(InvalidBodyPartsCoveredInPercentageException.class);

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
        double expectedPrize = 5.32 * 100; // there is 100 apples. Each Apple costs 5.32
        //when
        double actualPrize = applesItem.calculateTotalPrize();
        //then
        assertThat(actualPrize).isEqualTo(expectedPrize);
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerForTwoTurns_ThenGetThreeValue() {
        //given from setUp
        final int[] actualAttack = new int[1];
        final int expectedAttack = 3;

        Assertions.assertAll(
                //when
                () -> actualAttack[0] = weaponItem.getAttackPower(2),
                //then
                () -> assertThat(actualAttack[0]).isEqualTo(expectedAttack)
        );
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerWithMinusTurns_ThenThrowInvalidTurnsException() {
        //given from setUp
        //when in then
        //then
        assertThatThrownBy(() -> weaponItem.getAttackPower(-1)).isInstanceOf(InvalidTurnsException.class);
    }

    @Test
    void givenWeaponItem_WhenGetAttackPowerWithMoreThanThreeValue_ThenThrowInvalidTurnsException() {
        //given from setUp
        //when in then
        //then
        assertThatThrownBy(() -> weaponItem.getAttackPower(4)).isInstanceOf(InvalidTurnsException.class);
    }

    @Test
    void givenEmptyItem_WhenSetHeldIn_ThenSetProperly() {
        //given
        Item testItem = new Item();

        Assertions.assertAll(
                //when
                () -> testItem.setHeldIn(BodyType.RIGHT_ARM),
                //then
                () -> assertThat(testItem.getHeldIn()).isEqualTo(BodyType.RIGHT_ARM)
        );
    }

    @Test
    void givenApplesItem_WhenSetHeldIn_ThenSetProperly() {
        //given from setUp
        Assertions.assertAll(
                //when
                () -> applesItem.setHeldIn(BodyType.TORSO),
                //then
                () -> assertThat(applesItem.getHeldIn()).isEqualTo(BodyType.TORSO)
        );
    }

    @Test
    void givenWeaponItem_WhenSetHeldInRightHand_ThenSetProperly() {
        //given from setUp
        Assertions.assertAll(
                //when
                () -> weaponItem.setHeldIn(BodyType.RIGHT_ARM),
                //then
                () -> assertThat(weaponItem.getHeldIn()).isEqualTo(BodyType.RIGHT_ARM)
        );
    }

    @Test
    void givenWeaponItem_WhenSetHeldInNull_ThenSetProperly() {
        //given from setUp
        Assertions.assertAll(
                //when
                () -> weaponItem.setHeldIn(null),
                //then
                () -> assertThat(weaponItem.getHeldIn()).isNull()
        );
    }

    @Test
    void givenWeaponItem_WhenSetHeldInTorso_ThenThrowInvalidBodyTypeException() {
        //given from setUp
        //when
        //then
        assertThatThrownBy(() -> weaponItem.setHeldIn(BodyType.TORSO)).isInstanceOf(InvalidBodyTypeException.class);
    }

    @Test
    void givenShieldItem_WhenSetHeldInTorso_ThenThrowInvalidBodyTypeException() {
        //given from setUp
        //when
        //then
        assertThatThrownBy(() -> shieldItem.setHeldIn(BodyType.TORSO)).isInstanceOf(InvalidBodyTypeException.class);
    }

    @Test
    void givenShieldItem_WhenSetHeldInLeftArm_ThenSetProperly() {
        //given from setUp
        Assertions.assertAll(
                //when
                () -> shieldItem.setHeldIn(BodyType.LEFT_ARM),
                //then
                () -> assertThat(shieldItem.getHeldIn()).isEqualTo(BodyType.LEFT_ARM)
        );
    }

    @Test
    void givenBadCreatedArmorItem_WhenSetHeldIn_ThenThrowInvalidArmorBodyPartsCoveredInPercentage() {
        //given from setUp
        //when
        armorItem.setBodyPartsCoveredInPercentage(null);
        //then
        assertThatThrownBy(() -> armorItem.setHeldIn(BodyType.TORSO)).isInstanceOf(InvalidBodyPartsCoveredInPercentageException.class);
    }

    @Test
    void givenBadCreatedShieldItem_WhenSetHeldIn_ThenThrowInvalidArmorBodyPartsCoveredInPercentage() {
        //given from setUp
        //when
        shieldItem.setBodyPartsCoveredInPercentage(null);
        //then
        assertThatThrownBy(() -> shieldItem.setHeldIn(BodyType.LEFT_ARM)).isInstanceOf(InvalidBodyPartsCoveredInPercentageException.class);
    }

    @Test
    void givenShieldItem_WhenSetHeldInRightArm_ThenBodyPartsCoveredInPercentageForRightHandIsHigher() {
        //given from setUp
        //when
        Assertions.assertAll(
                () -> shieldItem.setHeldIn(BodyType.RIGHT_ARM),
                () -> assertThat(shieldItem.getBodyPartsCoveredInPercentage().get(BodyType.RIGHT_ARM))
                        .isGreaterThanOrEqualTo(shieldItem.getBodyPartsCoveredInPercentage().get(BodyType.LEFT_ARM))
        );

    }

    @Test
    void givenShieldItem_WhenSetHeldInLeftArm_ThenBodyPartsCoveredInPercentageForLeftHandIsHigher() {
        //given from setUp
        //when
        Assertions.assertAll(
                () -> shieldItem.setHeldIn(BodyType.LEFT_ARM),
                () -> assertThat(shieldItem.getBodyPartsCoveredInPercentage().get(BodyType.LEFT_LEG))
                        .isGreaterThanOrEqualTo(shieldItem.getBodyPartsCoveredInPercentage().get(BodyType.RIGHT_LEG))
        );
    }


}