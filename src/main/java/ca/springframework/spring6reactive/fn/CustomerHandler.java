package ca.springframework.spring6reactive.fn;

import ca.springframework.spring6reactive.model.CustomerDTO;
import ca.springframework.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Created by jt, Spring Framework Guru.
 */
@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService customerService;
    private final Validator validator;

    /**
     * Rest API — Functional Endpoint
     * Spring Functional Endpoint kullanarak API katmanını functional programlamaya uygun halde geliştirebiliriz.
     * Bu yapı ile birlikte routing ve istekleri işleyebileceğimiz bir yapı oluşturabiliriz. Bu cümleler ilk başta
     * bir anlam ifade etmeyebilir, o nedenle örneğe doğru ilerleyelim.
     *
     * Anotasyon bazlı model yerine burada HandlerFunction ve RouterFunctions olarak adlandıralan yapıları kullanıyoruz.
     *
     * HandlerFunction; “ServerRequest” türünde bir değişkeni alan ve “ServerResponse” türünde bir Mono
     * döndüren metottur. Anotasyon bazlı yapıya benzer bir örnek gerçekleştirmek için User Controller yapısına göre
     * HandlerFunction oluşturalım.
     *
     * @Component
     * public class UserHandler {
     *
     *     private final UserService userService;
     *     public UserHandler(UserService userService) {
     *         this.userService = userService;
     *     }
     *
     *     public Mono<ServerResponse> getUsers(ServerRequest request) {
     *         return ServerResponse.ok()
     *                 .contentType(MediaType.APPLICATION_JSON)
     *                 .body(userService.getUsers(), User.class);
     *     }
     * }
     * Handler fonksiyonları yarattığımızda bu metotlara gelen istekleri yönlendirebilecek Router
     * fonksiyonları RouterFunctions.route() metodu kullanılarak yazılabilir. Birden fazla router aynı
     * Router Function metodu içerisinde .andRoute(…) kullanılarak tanımlanabilir.
     *
     * @Configuration
     * public class RoutingHandler {
     *
     *     private static final String apiPrefix = "/api/v1/users";
     *
     *     @Bean
     *     public RouterFunction<ServerResponse> functionalRoutes(UserHandler userHandler) {
     *         return RouterFunctions
     *                 .route(RequestPredicates.GET(apiPrefix), userHandler::getUsers);
     *     }
     * }
     * Functional programlama ile Reactive programlama çok güzel bir ikilidir. Bu nedenle biz ne kadar anotasyon
     * bazlı programlamaya aşina olsak da functional programlamaya yönelik geliştirmeler için Router Functions’ları kullanabiliriz.
     * Böylece tüm routing yönetimini framework yerine kendimiz yapabilir hale geliriz.*/

    private void validate(CustomerDTO customerDTO){
        Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDto");
        validator.validate(customerDTO, errors);

        if (errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> deleteCustomerById(ServerRequest request){
        return customerService.getCustomerById(request.pathVariable("customerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(customerDTO -> customerService.deleteCustomerById(customerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchCustomerById(ServerRequest request){
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService
                        .patchCustomer(request.pathVariable("customerId"),customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateCustomerById(ServerRequest request) {
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService
                        .updateCustomer(request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> createNewCustomer(ServerRequest request){
        return customerService.saveNewCustomer(request.bodyToMono(CustomerDTO.class).doOnNext(this::validate))
                .flatMap(customerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_PATH_ID)
                                .build(customerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request){
        return ServerResponse
                .ok()
                .body(customerService.getCustomerById(request.pathVariable("customerId"))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        CustomerDTO.class);
    }

    public Mono<ServerResponse> listCustomers(ServerRequest request){

        return ServerResponse.ok()
                .body(customerService.listCustomers(), CustomerDTO.class);
    }
}
