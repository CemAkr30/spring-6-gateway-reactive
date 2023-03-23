package ca.springframework.spring6reactive.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Created by jt, Spring Framework Guru.
 */
@Configuration
@RequiredArgsConstructor
public class BeerRouterConfig {
    public static final String BEER_PATH = "/api/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    public static final String BEER_SAVE_PATH = "/api/beer/save";

    public static final String BEER_UPDATE_PATH = "/api/update";

    public static final String BEER_DELETE_PATH = "/api/delete";

    public static final String BEER_PATCH_PATH = "/api/patch";

    private final BeerHandler handler;

    @Bean
    public RouterFunction<ServerResponse> beerRoutes(){
        return route()
                .GET(BEER_PATH, accept(APPLICATION_JSON), handler::listBeers)
                .GET(BEER_PATH_ID, accept(APPLICATION_JSON), handler::getBeerById)
                .POST(BEER_SAVE_PATH, accept(APPLICATION_JSON), handler::createNewBeer)
                .PUT(BEER_UPDATE_PATH, accept(APPLICATION_JSON), handler::updateBeerById)
                .PATCH(BEER_PATCH_PATH, accept(APPLICATION_JSON), handler::patchBeerById)
                .DELETE(BEER_DELETE_PATH, accept(APPLICATION_JSON), handler::deleteBeerById)
                .build();
    }

}
