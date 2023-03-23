package ca.springframework.spring6reactive.mappers;


import ca.springframework.spring6reactive.domain.Customer;
import ca.springframework.spring6reactive.model.CustomerDTO;
import org.mapstruct.Mapper;

/**
 * Created by jt, Spring Framework Guru.
 */
@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
