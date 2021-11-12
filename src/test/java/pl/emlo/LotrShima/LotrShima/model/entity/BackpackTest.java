package pl.emlo.LotrShima.LotrShima.model.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BackpackTest {
    private Backpack backpack;

    @BeforeEach
    void setUp() {
        backpack = Backpack.builder()
                .cash(0)
                .build();
    }


    @Test
    void givenBackpack_WhenGetItemsByItemType_ThenResultsHasGoodSize() {
        //given
        Item apples = Item.builder().name("apples").type(ItemType.FOOD).build();
        Item bananas = Item.builder().name("bananas").type(ItemType.FOOD).build();
        Item sword = Item.builder().name("sword").powerOfItem(List.of(1, 2, 3)).type(ItemType.WEAPON).build();
        backpack.setItems(List.of(apples, bananas, sword));

        //when
        List<Item> actualFood = backpack.getItemsByItemType(ItemType.FOOD);
        List<Item> actualWeapon = backpack.getItemsByItemType(ItemType.WEAPON);

        //then
        Assertions.assertAll(

                () -> assertThat(actualFood).hasSize(2),
                () -> assertThat(actualWeapon).hasSize(1)
        );

    }

    @Test
    void givenBackpack_WhenGetTotalWeight_ThenReturnWeightOfAllItems() {
        //given
        Item apples = Item.builder().name("apples").quantity(200).weight(1).type(ItemType.FOOD).build();
        Item bananas = Item.builder().name("bananas").quantity(100).weight(5).type(ItemType.FOOD).build();
        Item sword = Item.builder().name("sword").weight(2000).type(ItemType.WEAPON).build();
        int expectedWeight = 200+500+2000;
        backpack.setItems(List.of(apples,bananas,sword));

        //when
        //then
        assertThat(backpack.getTotalWeight()).isEqualTo(expectedWeight);
    }

    @Test
    void givenBackpackWithAddedItem_WhenAddItem_ThenChangeQuantityOfItemInBackPack() {
        //given
        final String APPLES = "apples";
        Item apples = Item.builder().name(APPLES).quantity(200).weight(1).type(ItemType.FOOD).build();
        Item addedApples = Item.builder().name(APPLES).quantity(50).weight(1).type(ItemType.FOOD).build();
        backpack.setItems(List.of(apples));

        //when
        backpack.addItem(addedApples);
        //then
        Assertions.assertAll(
                () -> assertThat(backpack.getItems()).hasSize(1),
                () -> assertThat(backpack.getItems().get(0).getQuantity()).isEqualTo(250)
        );
    }
    @Test
    void givenBackpackNoItemsIn_WhenAddItem_ThenAddNewItems() {
        //given
        Item addedApples = Item.builder().name("apples").quantity(200).weight(1).type(ItemType.FOOD).build();
        Item addedBananas = Item.builder().name("bananas").quantity(100).weight(5).type(ItemType.FOOD).build();
        Item addedSword = Item.builder().name("sword").weight(2000).type(ItemType.WEAPON).build();


        //when
        backpack.addItem(addedApples);
        backpack.addItem(addedBananas);
        backpack.addItem(addedSword);
        //then
        Assertions.assertAll(
                () -> assertThat(backpack.getItems()).hasSize(3),
                () -> assertThat(backpack.getItems().get(0).getQuantity()).isEqualTo(200),
                () -> assertThat(backpack.getItems().get(1).getQuantity()).isEqualTo(100),
                () -> assertThat(backpack.getItems().get(2).getQuantity()).isOne()
        );
    }
}