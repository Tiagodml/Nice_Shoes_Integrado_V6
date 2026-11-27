package br.com.niceshoes.repository;
import br.com.niceshoes.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsByCliente_Id(Long id);
    boolean existsByProduto_Id(Long id);
}
