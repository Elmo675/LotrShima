package pl.emlo.LotrShima.LotrShima.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.emlo.LotrShima.LotrShima.exceptions.BackpackNotFoundException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidCashException;
import pl.emlo.LotrShima.LotrShima.exceptions.InvalidQuantityException;
import pl.emlo.LotrShima.LotrShima.exceptions.ItemNotFoundException;
import pl.emlo.LotrShima.LotrShima.model.entity.Backpack;
import pl.emlo.LotrShima.LotrShima.model.entity.Item;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;
import pl.emlo.LotrShima.LotrShima.repository.BackpackRepository;

@Service
@AllArgsConstructor
public class BackpackService {

    @Autowired
    private BackpackRepository backpackRepository;


    public Backpack getBackpackById(Long backpackId) throws BackpackNotFoundException {
        return backpackRepository.findById(backpackId)
                .orElseThrow(() -> new BackpackNotFoundException(backpackId));
    }

    public Page<Item> getItemPage(Long backpackId, Pageable pageable) {
        return backpackRepository.getItems(backpackId, pageable);
    }

    public Page<Item> getItemPageByType(Long backpackId, ItemType type, Pageable pageable) {
        return backpackRepository.getItemsByItemType(backpackId, type, pageable);
    }

    public int getTotalWeight(Long backpackId) {
        return backpackRepository.getTotalWeight(backpackId);
    }

    public double getCash(Long backpackId) {
        return backpackRepository.getCash(backpackId);
    }

    public void addItem(Long backpackId, Item item) throws BackpackNotFoundException {
        Backpack backpack = getBackpackById(backpackId);
        backpack.addItem(item);
        backpackRepository.save(backpack);
    }

    public void payCash(Long backpackId, double cash) throws BackpackNotFoundException, InvalidCashException {
        Backpack backpack = getBackpackById(backpackId);
        lowerBackpackCashBy(cash, backpack);
        backpackRepository.save(backpack);
    }

    public void buyItem(Long backpackId, Item item) throws BackpackNotFoundException, InvalidCashException {
        Backpack backpack = getBackpackById(backpackId);
        lowerBackpackCashBy(item.calculateTotalPrize(), backpack);
        backpack.addItem(item);
        backpackRepository.save(backpack);
    }

    private void lowerBackpackCashBy(double cash, Backpack backpack) throws InvalidCashException {
        if (backpack.getCash() - cash < 0) {
            throw new InvalidCashException();
        }
        backpack.setCash(backpack.getCash() - cash);
    }

    public void gainCash(Long backpackId, double cash) throws BackpackNotFoundException {
        Backpack backpack = getBackpackById(backpackId);
        backpack.setCash(backpack.getCash() + cash);
        backpackRepository.save(backpack);
    }

    public void sellItem(Long backpackId, String itemName, int quantity) throws BackpackNotFoundException, ItemNotFoundException, InvalidQuantityException {
        Backpack backpack = getBackpackById(backpackId);
        Item soldItem = getItemFromBackpackByNameAndLowerQuantity(itemName, quantity, backpack);
        double gainedCash = soldItem.getPrize() * quantity;
        backpack.setCash(backpack.getCash() + gainedCash);
        backpackRepository.save(backpack);
    }

    public void sellItem(Long backpackId, String itemName) throws ItemNotFoundException, BackpackNotFoundException, InvalidQuantityException {
        Item soldItem = backpackRepository.getItemByName(backpackId, itemName).orElseThrow(() -> new ItemNotFoundException(itemName));
        sellItem(backpackId, itemName, soldItem.getQuantity());
    }

    public void removeItem(Long backpackId, String itemName, int quantity) throws ItemNotFoundException, InvalidQuantityException, BackpackNotFoundException {
        Backpack backpack = getBackpackById(backpackId);
        Item removedItem = getItemFromBackpackByNameAndLowerQuantity(itemName, quantity, backpack);
        backpackRepository.save(backpack);
    }

    public void removeItem(Long backpackId, String itemName) throws ItemNotFoundException, BackpackNotFoundException {
        Backpack backpack = getBackpackById(backpackId);
        Item removedItem = backpackRepository.getItemByName(backpackId, itemName).orElseThrow(() -> new ItemNotFoundException(itemName));
        backpack.getItems().remove(removedItem);
        backpackRepository.save(backpack);
    }

    private Item getItemFromBackpackByNameAndLowerQuantity(String itemName, int quantity, Backpack backpack) throws ItemNotFoundException, InvalidQuantityException {
        Item soldItem = backpackRepository.getItemByName(backpack.getId(), itemName).orElseThrow(() -> new ItemNotFoundException(itemName));
        if (soldItem.getQuantity() < quantity) {
            throw new InvalidQuantityException(itemName);
        }
        int quantityLeft = soldItem.getQuantity() - quantity;
        if (quantityLeft == 0) {
            backpack.getItems().remove(soldItem);
        } else {
            backpack.getItems().get(
                    backpack.getItems().indexOf(soldItem)
            ).setQuantity(quantityLeft);
        }
        return soldItem;
    }


}
