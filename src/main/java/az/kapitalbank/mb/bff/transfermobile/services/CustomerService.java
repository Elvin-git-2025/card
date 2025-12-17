package az.kapitalbank.mb.bff.transfermobile.services;


import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCustomerRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CustomerResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Customer;
import az.kapitalbank.mb.bff.transfermobile.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = Customer.builder()
                .firstName(createCustomerRequest.getFirstName())
                .lastName(createCustomerRequest.getLastName())
                .pin(createCustomerRequest.getPin())
                .dateOfBirth(createCustomerRequest.getDateOfBirth())
                .createdAt(createCustomerRequest.getCreatedAt())
                .build();

        Customer savedCustomer=customerRepository.save(customer);

         return CustomerResponse.builder()
                .id(savedCustomer.getId())
                .firstName(savedCustomer.getFirstName())
                .lastName(savedCustomer.getLastName())
                .age(savedCustomer.getAge()) // dynamic calculation
                .pin(savedCustomer.getPin())
                .dateOfBirth(savedCustomer.getDateOfBirth())
                .createdAt(savedCustomer.getCreatedAt())
                .build();

    }


    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
      return   CustomerResponse.builder()
              .firstName(customer.getFirstName())
              .lastName(customer.getLastName())
              .age(customer.getAge())
              .pin(customer.getPin())
              .build();
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .age(customer.getAge()) // dynamically calculated
                        .pin(customer.getPin())
                        .dateOfBirth(customer.getDateOfBirth())
                        .createdAt(customer.getCreatedAt())
                        .build())
                .toList();
    }



    public Customer update(Long id,Customer customer) {
        Customer updatedCustomer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        updatedCustomer.setFirstName(customer.getFirstName());
        updatedCustomer.setLastName(customer.getLastName());
        updatedCustomer.setPin(customer.getPin());
        updatedCustomer.setAge(customer.getAge());
        return customerRepository.save(updatedCustomer);
    }

    public void delete(Long id) {
        customerRepository.delete(customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found")));
    }
}
