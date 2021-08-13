package pizza.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class Pizza {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min = 3, message = "Name must be at least 3 charters long")
    private String name;

    @ManyToMany(targetEntity = Ingredient.class)
    //@NotNull or @NotEmpty for list.size < 2
    //for size > 2 or with @ManyToMany
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    private List<Ingredient> ingredients;

    @PrePersist
    public void setUpCreatedAt() {
        this.createdAt = new Date();
    }
}
