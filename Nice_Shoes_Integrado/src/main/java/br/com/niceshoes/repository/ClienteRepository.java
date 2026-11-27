package br.com.niceshoes.repository;
import br.com.niceshoes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
