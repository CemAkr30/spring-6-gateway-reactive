package ca.springframework.spring6reactive.repositories;


import ca.springframework.spring6reactive.domain.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Created by jt, Spring Framework Guru.
 */
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
