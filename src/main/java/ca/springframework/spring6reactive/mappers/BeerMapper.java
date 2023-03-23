package ca.springframework.spring6reactive.mappers;


import ca.springframework.spring6reactive.domain.Beer;
import ca.springframework.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

/**
 * Created by jt, Spring Framework Guru.
 */
@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDTO beerDTO);
}
