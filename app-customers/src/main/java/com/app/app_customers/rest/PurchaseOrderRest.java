package com.app.app_customers.rest;

import com.app.app_customers.cliente.BookRestClient;
import com.app.app_customers.db.PurchaseOrder;
import com.app.app_customers.dto.CustomerDto;
import com.app.app_customers.dto.PurchaseOrderDto;
import com.app.app_customers.repo.CustomersRepository;
import com.app.app_customers.repo.PurchaseOrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Transactional
public class PurchaseOrderRest {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderRest.class);

    @Value("${server.port}")
    private Integer httpPort;

    @Value("${services.books.url}")
    private String booksServiceUrl;

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private CustomersRepository customerRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestClient.Builder clientBuilder;

    private BookRestClient createBookClient() {
        RestClient restClient = clientBuilder
                .baseUrl(booksServiceUrl + "/books")
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(BookRestClient.class);
    }

    private PurchaseOrderDto map(PurchaseOrder source) {
        var dto = mapper.map(source, PurchaseOrderDto.class);
        BookRestClient client = createBookClient();

        dto.getLineItems().forEach(item -> {
            try {
                var book = client.findByBook(item.getIsbn());
                item.setTitle(book.getTitle());
                item.setPrice(book.getPrice());
                item.setAuthors(book.getAuthors());
            } catch (Exception e) {
                log.warn("No se pudo obtener el libro con ISBN {}", item.getIsbn(), e);
                item.setTitle("Desconocido");
                item.setPrice(BigDecimal.ZERO);
                item.setAuthors(Collections.emptyList());
            }
        });

        return dto;
    }

    @GetMapping("/customer/{customerId}")
    public List<PurchaseOrderDto> ordersByCustomer(@PathVariable Integer customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PurchaseOrderDto> orderDetail(@PathVariable Integer orderId) {
        return repository.findById(orderId)
                .map(this::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody PurchaseOrder order) {
        repository.save(order);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> update(@PathVariable Integer orderId, @RequestBody PurchaseOrder order) {
        if (repository.existsById(orderId)) {
            order.setId(orderId);
            repository.save(order);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }
}
