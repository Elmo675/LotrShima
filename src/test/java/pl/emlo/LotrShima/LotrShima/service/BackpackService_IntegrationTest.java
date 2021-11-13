package pl.emlo.LotrShima.LotrShima.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.emlo.LotrShima.LotrShima.exceptions.BackpackNotFoundException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidCashException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidQuantityException;
import pl.emlo.LotrShima.LotrShima.exceptions.ItemNotFoundException;
import pl.emlo.LotrShima.LotrShima.model.entity.Backpack;
import pl.emlo.LotrShima.LotrShima.model.entity.Item;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;
import pl.emlo.LotrShima.LotrShima.repository.BackpackRepository;
import pl.emlo.LotrShima.LotrShima.utils.ItemUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BackpackService_IntegrationTest {

    @Autowired
    BackpackRepository backpackRepository;

    BackpackService service;

    @BeforeAll
    void setUp() {
        service = new BackpackService(backpackRepository);
    }

    @Test
    void itemPage_ShouldReturnAllItemsPage() {
        //given
        Item item1 = ItemUtils.getApplesItem();
        Item item2 = ItemUtils.getHealingItem();
        Item item3 = ItemUtils.getArmorItem();
        Backpack givenBackpack = Backpack.builder().build();
        givenBackpack.setItems(List.of(item1, item2, item3));
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        Page<Item> expectedPage = service.getItemPage(givenBackpack.getId(), PageRequest.of(0, 10));
        //then
        assertThat(expectedPage.getTotalElements()).isEqualTo(3);
    }

    @Test
    void itemPageByType_ShouldReturnPageOfOneArmorItem() {
        //given
        Item item1 = ItemUtils.getApplesItem();
        Item item2 = ItemUtils.getHealingItem();
        Item item3 = ItemUtils.getArmorItem();
        Backpack givenBackpack = Backpack.builder().items(List.of(item1, item2, item3)).build();
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        ItemType searchedItemType = ItemType.ARMOR;
        Page<Item> expectedPage = service.getItemPageByType(givenBackpack.getId(), searchedItemType, PageRequest.of(0, 10));
        //then
        assertThat(expectedPage.getTotalElements()).isOne();
    }

    @Test
    void getTotalWeight_ShouldReturnTotalWeightOfItems() {
        Item item1 = ItemUtils.getApplesItem();
        Item item2 = ItemUtils.getWeaponItem();
        Integer givenTotalWeight = item1.getWeight() * item1.getQuantity()
                + item2.getWeight() * item2.getQuantity();
        Backpack givenBackpack = Backpack.builder().items(List.of(item1, item2)).build();
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        int expectedTotalWeight = service.getTotalWeight(givenBackpack.getId());
        //then
        assertThat(expectedTotalWeight).isEqualTo(givenTotalWeight);
    }

    @Test
    void getCash_ShouldReturnTotalCash() {
        //given
        double givenCash = 1500.32;
        Backpack givenBackpack = Backpack.builder().cash(givenCash).build();
        Backpack anotherBackpack = Backpack.builder().cash(500.00).build();
        givenBackpack = backpackRepository.save(givenBackpack);
        backpackRepository.save(anotherBackpack);
        //when
        double expectedCash = service.getCash(givenBackpack.getId());
        //then
        assertThat(expectedCash).isEqualTo(givenCash);
    }

    @Test
    void addItem_ShouldAddNewItemToBackpack() throws BackpackNotFoundException {
        //given
        Item addedItem = ItemUtils.getArmorItem();
        Backpack givenBackpack = Backpack.builder().build();
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        service.addItem(givenBackpack.getId(), addedItem);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        assertThat(expectedBackpack.getItems().size()).isOne();
    }

    @Test
    void addItem_ShouldThrowException_WhenBackpackWithGivenIdDoesNotExist() {
        //given
        Item addedItem = ItemUtils.getArmorItem();
        Backpack givenBackpack = Backpack.builder().build();
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        Long wrongId = givenBackpack.getId() + 1;
        //then
        assertThatThrownBy(() -> service.addItem(wrongId, addedItem)).isExactlyInstanceOf(BackpackNotFoundException.class);

    }

    @Test
    void addItem_ShouldChangeQuantityOfItemInBackpack_WhenAddedItemIsInBackpack() throws BackpackNotFoundException {
        //given
        Item addedItem = ItemUtils.getWeaponItem();
        ArrayList<Item> givenItems = new ArrayList<>();
        givenItems.add(addedItem);
        Backpack givenBackpack = Backpack.builder().items(givenItems).build();
        givenBackpack = backpackRepository.save(givenBackpack);
        int givenQuantity = givenBackpack.getItems().get(0).getQuantity();
        //when
        service.addItem(givenBackpack.getId(), addedItem);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getItems().size()).isOne(),
                () -> assertThat(expectedBackpack.getItems().get(0).getQuantity()).isGreaterThan(givenQuantity)
        );
    }

    @Test
    void payCash_ShouldLowerCashInBackpack_WhenCashInBackpackIsBiggerThanPayed() throws BackpackNotFoundException, InvalidCashException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(500).build();
        double givenPayedCash = 250.00;
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        service.payCash(givenBackpack.getId(), givenPayedCash);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        assertThat(expectedBackpack.getCash()).isEqualTo(250.00);
    }

    @Test
    void payCash_ShouldThrowException_WhenCashInBackpackIsLowerThanPayed() {
        //given
        Backpack givenBackpack = Backpack.builder().cash(500).build();
        double givenPayedCash = 750.00;
        givenBackpack = backpackRepository.save(givenBackpack);
        Long givenBackpackId = givenBackpack.getId();
        //when
        //then
        assertThatThrownBy(() -> service.payCash(givenBackpackId, givenPayedCash)).isExactlyInstanceOf(InvalidCashException.class);
    }

    @Test
    void gainCash_ShouldRaiseCashInBackpack() throws BackpackNotFoundException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(500).build();
        double givenGainCash = 250.00;
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        service.gainCash(givenBackpack.getId(), givenGainCash);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        assertThat(expectedBackpack.getCash()).isEqualTo(750.00);
    }

    @Test
    void buyItem_ShouldLowerCashAndAddItemToBackpack_WhenThereIsEnoughCashAndNoItemInBackpack() throws BackpackNotFoundException, InvalidCashException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(500).build();
        double givenPayedCash = 250.00;

        Item itemToBought = ItemUtils.getWeaponItem();
        itemToBought.setPrize(givenPayedCash);

        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        service.buyItem(givenBackpack.getId(), itemToBought);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getCash()).isEqualTo(250.00),
                () -> assertThat(expectedBackpack.getItems().size()).isOne(),
                () -> assertThat(expectedBackpack.getItems().get(0)).isEqualTo(itemToBought),
                () -> assertThat(expectedBackpack.getItems().get(0).getQuantity()).isOne() //WeaponItem has 1 quantity
        );
    }

    @Test
    void buyItem_ShouldLowerCashAndRiseQuantityOfItem_WhenThereIsEnoughCashAndItemIsInTheBackpack() throws BackpackNotFoundException, InvalidCashException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(500).build();
        double givenPayedCash = 250.00;

        Item itemToBought = ItemUtils.getWeaponItem();
        itemToBought.setPrize(givenPayedCash);
        givenBackpack.getItems().add(itemToBought);

        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        service.buyItem(givenBackpack.getId(), itemToBought);
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getCash()).isEqualTo(250.00),
                () -> assertThat(expectedBackpack.getItems().size()).isOne(),
                () -> assertThat(expectedBackpack.getItems().get(0)).isEqualTo(itemToBought),
                () -> assertThat(expectedBackpack.getItems().get(0).getQuantity()).isEqualTo(2) // there was 1 item in backpack, after buyItem there are 2
        );
    }

    @Test
    void sellItem_ShouldRiseCashAndLowerQuantity_WhenSellingLessQuantityThanInBackPack() throws BackpackNotFoundException, InvalidQuantityException, ItemNotFoundException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(1000).build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        double itemPrize = itemInBackpack.getPrize();
        //when
        String itemInBackpackName = itemInBackpack.getName();
        service.sellItem(givenBackpack.getId(), itemInBackpackName, 1); //we sell 1 item
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getCash()).isEqualTo(1000 + itemPrize),
                () -> assertThat(expectedBackpack.getItems().size()).isOne(),
                () -> assertThat(expectedBackpack.getItems().get(0).getQuantity()).isOne()
        );
    }

    @Test
    void sellItem_ShouldRiseCashAndDeleteItem_WhenSellingSameQuantityAsInBackpack() throws BackpackNotFoundException, InvalidQuantityException, ItemNotFoundException {
        //given
        Backpack givenBackpack = Backpack.builder().cash(1000).build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        double itemPrize = itemInBackpack.getPrize();
        //when
        String itemInBackpackName = itemInBackpack.getName();
        service.sellItem(givenBackpack.getId(), itemInBackpackName); //we sell 2 items
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getCash()).isEqualTo(1000 + 2 * itemPrize),
                () -> assertThat(expectedBackpack.getItems().size()).isZero()
        );
    }

    @Test
    void sellItem_ShouldThrowException_WhenSellingMoreQuantityThanInBackpack() {
        //given
        Backpack givenBackpack = Backpack.builder().cash(1000).build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        Long backpackId = givenBackpack.getId();
        //when
        //then
        String itemInBackpackName = itemInBackpack.getName();
        assertThatThrownBy(
                () -> service.sellItem(backpackId, itemInBackpackName, 3) // we try to sell 3 items
        ).isExactlyInstanceOf(InvalidQuantityException.class);

    }

    @Test
    void sellItem_ShouldThrowException_WhenSellingItemThatWeDoNotHaveInBackpack() {
        //given
        Backpack givenBackpack = Backpack.builder().cash(1000).build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        Long backpackId = givenBackpack.getId();
        //when
        //then
        String otherName = "Other Name";
        assertThatThrownBy(
                () -> service.sellItem(backpackId, otherName, 1)
        ).isExactlyInstanceOf(ItemNotFoundException.class);
    }

    @Test
    void removeItem_ShouldRemoveItem_WhenRemovingItemThatIsInBackpack() throws ItemNotFoundException, BackpackNotFoundException {
        //given
        Backpack givenBackpack = Backpack.builder().build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        String itemInBackpackName = itemInBackpack.getName();
        service.removeItem(givenBackpack.getId(), itemInBackpackName); //we remove 2 items
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        assertThat(expectedBackpack.getItems().size()).isZero();
    }

    @Test
    void removeItem_ShouldLowerQuantity_WhenRemovingLessItemsThanInBackpack() throws ItemNotFoundException, BackpackNotFoundException, InvalidQuantityException {
        //given
        Backpack givenBackpack = Backpack.builder().build();
        Item itemInBackpack = ItemUtils.getWeaponItem();
        itemInBackpack.setQuantity(2); // we have 2 items in backpack
        givenBackpack.getItems().add(itemInBackpack);
        givenBackpack = backpackRepository.save(givenBackpack);
        //when
        String itemInBackpackName = itemInBackpack.getName();
        service.removeItem(givenBackpack.getId(), itemInBackpackName, 1); //we remove 1 items
        //then
        Backpack expectedBackpack = backpackRepository.getById(givenBackpack.getId());
        Assertions.assertAll(
                () -> assertThat(expectedBackpack.getItems().size()).isOne(),
                () -> assertThat(expectedBackpack.getItems().get(0).getQuantity()).isOne()
        );
    }
}