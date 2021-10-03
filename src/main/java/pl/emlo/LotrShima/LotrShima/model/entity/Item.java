package pl.emlo.LotrShima.LotrShima.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;


}
