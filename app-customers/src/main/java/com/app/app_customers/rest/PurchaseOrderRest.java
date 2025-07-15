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
import com.app.app_customers.db.Customer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Transactional
@CrossOrigin
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
                .baseUrl(booksServiceUrl) //.baseUrl(booksServiceUrl + "/books")
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

        BigDecimal total = dto.getLineItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotal(total);
        return dto;
    }

    // http://localhost:7070/orders/customer/1
    @GetMapping("/customer/{customerId}")
    public List<PurchaseOrderDto> ordersByCustomer(@PathVariable Integer customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    // http://localhost:7070/orders/1
    @GetMapping("/{orderId}")
    public ResponseEntity<PurchaseOrderDto> orderDetail(@PathVariable Integer orderId) {
        return repository.findById(orderId)
                .map(this::map)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody PurchaseOrder order) {
        order.setId(null);

        // Manejo del cliente
        if (order.getCustomer() != null && order.getCustomer().getId() == null) {
            order.getCustomer().setId(null);
            Customer savedCustomer = customerRepository.save(order.getCustomer());
            order.setCustomer(savedCustomer);
        } else if (order.getCustomer() != null && order.getCustomer().getId() != null) {
            Customer existingCustomer = customerRepository.findById(order.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer no encontrado"));
            order.setCustomer(existingCustomer);
        }

        if (order.getLineItems() != null) {
            order.getLineItems().forEach(item -> {
                item.setId(null);
                item.setPurchaseOrder(order);
            });
        }

        repository.save(order);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> update(@PathVariable Integer orderId, @RequestBody PurchaseOrder order) {
        if (!repository.existsById(orderId)) {
            return ResponseEntity.status(404).build();
        }

        // Establecer el ID de la orden
        order.setId(orderId);

        // Manejar el customer
        if (order.getCustomer() != null) {
            if (order.getCustomer().getId() == null) {
                // Customer nuevo - crearlo
                order.getCustomer().setId(null);
                Customer savedCustomer = customerRepository.save(order.getCustomer());
                order.setCustomer(savedCustomer);
            } else {
                // Customer existente - validar que existe
                Customer existingCustomer = customerRepository.findById(order.getCustomer().getId())
                        .orElseThrow(() -> new RuntimeException("Customer no encontrado"));
                order.setCustomer(existingCustomer);
            }
        }

        // Manejar LineItems - asegurar que no tengan IDs establecidos para evitar conflictos
        if (order.getLineItems() != null) {
            order.getLineItems().forEach(item -> item.setId(null));
        }

        repository.save(order);
        return ResponseEntity.ok().build();
    }

    // http://localhost:7070/orders/customers
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }

    //Eliminar customer
    // http://localhost:7070/orders/customers/{customerId}
    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(404).build();
        }

        // Verificar si el customer tiene órdenes asociadas
        List<PurchaseOrder> customerOrders = repository.findByCustomerId(customerId);
        if (!customerOrders.isEmpty()) {
            return ResponseEntity.status(409).build(); // Conflict - tiene órdenes asociadas
        }

        customerRepository.deleteById(customerId);
        return ResponseEntity.ok().build();
    }

    //  Ver todas las órdenes
    // http://localhost:7070/orders
    @GetMapping
    public List<PurchaseOrderDto> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // DELETE - Eliminar orden
    // http://localhost:7070/orders/2
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable Integer orderId) {
        if (!repository.existsById(orderId)) {
            return ResponseEntity.status(404).build();
        }

        repository.deleteById(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customers")
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        customer.setId(null); // evitar conflictos
        customerRepository.save(customer);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/customers/{customerId}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Integer customerId, @RequestBody CustomerDto customerDto) {
        if (!customerRepository.existsById(customerId)) {
            return ResponseEntity.status(404).build();
        }
        Customer customer = mapper.map(customerDto, Customer.class);
        customer.setId(customerId);

        customerRepository.save(customer);
        return ResponseEntity.ok().build();
    }

}
