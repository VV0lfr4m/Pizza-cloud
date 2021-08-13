package pizza.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pizza.configuration.OrderProp;
import pizza.model.Order;
import pizza.model.User;
import pizza.repositories.OrderRepo;
import pizza.repositories.jpa$jdbc.OrderRepoAdv;
import pizza.repositories.jpa.JpaOrderRepo;
import pizza.repositories.jpa.JpaUserRepo;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private final OrderProp orderProp;

    private final OrderRepo orderRepo;

    private final JpaUserRepo userRepo;

    private final OrderRepoAdv customOrderRepo;

    @Autowired
    public OrderController(OrderRepo orderRepo, JpaUserRepo userRepo,
                           OrderProp orderProp, OrderRepoAdv customOrderRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderProp = orderProp;
        this.customOrderRepo = customOrderRepo;
    }

    @GetMapping("/current")
    public String orderForm() {
        log.info("Displaying order form");
        return "orderForm";
    }

    @GetMapping
    public String ordersForUser (@AuthenticationPrincipal User user, Model model) {
        Pageable pageable = PageRequest.of(0, orderProp.getPageSize());
        model.addAttribute(
                "orders",
                customOrderRepo.findByUserOrderByPlacedAtDesc(user, pageable));
        return "orderList";
    }


    @PostMapping
    public String processOrder(@Valid Order order, Errors errors,
                               SessionStatus sessionStatus,
                               Principal principal) {

        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + order);

        User user = userRepo.findByUsername(principal.getName());
        order.setUser(user);

        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @PutMapping(path = "/{orderId}", consumes = "application/json")
    public ResponseEntity<Order> putOrder(@RequestBody Order order) {
        return customOrderRepo.customSave(order) != null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public ResponseEntity<Order> patchOrder(@PathVariable("orderId") Long orderId,
                            @RequestBody Order patch) {
        Order order = customOrderRepo.findById(orderId).orElseThrow(IllegalArgumentException::new);

        processPatchFields(order, patch);

        return customOrderRepo.customSave(order) != null
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{orderId}")
    private ResponseEntity<Order> deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            Optional<Order> deleted = customOrderRepo.customDeleteById(orderId);
            return new ResponseEntity<>(
                    deleted.orElseThrow(IllegalArgumentException::new),
                    HttpStatus.NO_CONTENT);

        }catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    private void processPatchFields(Order order, Order patch) {
        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(patch.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(patch.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(patch.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(patch.getDeliveryState());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
    }
}

