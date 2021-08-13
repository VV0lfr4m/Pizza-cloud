package pizza.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class CollectionModelResponseEntity extends ResponseEntity<CollectionModel> {
    public CollectionModelResponseEntity(CollectionModel body, HttpStatus status) {
        super(body, status);
    }
}
