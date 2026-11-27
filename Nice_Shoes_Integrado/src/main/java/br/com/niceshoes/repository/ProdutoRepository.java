package br.com.niceshoes.repository;
import br.com.niceshoes.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
