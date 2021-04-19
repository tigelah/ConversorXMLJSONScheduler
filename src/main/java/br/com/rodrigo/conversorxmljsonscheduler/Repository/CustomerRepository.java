package br.com.rodrigo.conversorxmljsonscheduler.Repository;

import br.com.rodrigo.conversorxmljsonscheduler.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
