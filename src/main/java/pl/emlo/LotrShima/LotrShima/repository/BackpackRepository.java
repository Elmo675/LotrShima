package pl.emlo.LotrShima.LotrShima.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.emlo.LotrShima.LotrShima.model.entity.Backpack;
import pl.emlo.LotrShima.LotrShima.model.entity.Item;
import pl.emlo.LotrShima.LotrShima.model.enums.ItemType;

import java.util.Optional;

@Repository
public interface BackpackRepository extends JpaRepository<Backpack, Long> {

    @Query(value = "SELECT i FROM Backpack b JOIN b.items i WHERE b.id = ?1")
    Page<Item> getItems(Long backpackId, Pageable pageable);

    @Query(value = "SELECT i FROM Backpack b JOIN b.items i WHERE b.id = ?1 AND i.type = ?2")
    Page<Item> getItemsByItemType(Long backpackId, ItemType type, Pageable pageable);

    @Query(value = "SELECT SUM(i.quantity*i.weight) FROM Backpack b JOIN b.items i WHERE b.id = ?1")
    Integer getTotalWeight(Long backpackId);

    @Query(value = "SELECT b.cash FROM Backpack b WHERE b.id = ?1")
    Double getCash(Long backpackId);

    @Query(value = "SELECT i FROM Backpack b JOIN b.items i WHERE b.id = ?1 AND i.name = ?2")
    Optional<Item> getItemByName(Long backpackId, String name);
}
