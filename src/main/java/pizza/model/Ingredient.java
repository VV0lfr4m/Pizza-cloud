package pizza.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
//force param -> create final properties as null or not
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    private final String id;
    private final String name;
    //Enums with JPA can use different fetch methods with the @Enumerated annotation.
    @Enumerated(EnumType.STRING)
    private final Type type;



    //static is unnecessary here
    public enum Type {
        DOUGH, CHEESE, PROTEIN, VEGGIES, SAUCE
    }
}
