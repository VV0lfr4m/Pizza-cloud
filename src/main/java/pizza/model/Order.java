package pizza.model;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "pizza_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    //Specifies that the property or field is not persistent
    @Transient
    @ManyToOne
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date placedAt;

    @Transient
    @ManyToMany(targetEntity = Pizza.class)
    private List<Pizza> designs = new ArrayList<>();

    @NotBlank(message = "Name is required")
    @Column(name = "delivery_name")
    private String deliveryName;

    @NotBlank(message = "Street is required")
    @Column(name = "delivery_street")
    private String deliveryStreet;

    @NotBlank(message = "City is required")
    @Column(name = "delivery_city")
    private String deliveryCity;

    @NotBlank(message = "State is required")
    @Column(name = "delivery_state")
    private String deliveryState;

    @NotBlank(message = "Zip is required")
    @Column(name = "delivery_zip")
    private String deliveryZip;

    @NotBlank
    @CreditCardNumber(message = "Not a valid credit card number")
    @Column(name = "cc_number")
    private String ccNumber;

    @NotBlank
    @Pattern(regexp = "^(0[1-9]|1[0-2])([/])([1-9][0-9])$",message = "Must be formatted MM/YY")
    @Column(name = "cc_expiration")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Not a valid credit card cvv")
    @Column(name = "cc_cvv")
    private String ccCVV;

    @PrePersist
    public void setUpPlacedAt() {
        this.placedAt = new Date();
    }

    public void addDesign(Pizza design) {
        this.designs.add(design);
    }
}
