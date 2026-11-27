package br.com.niceshoes;
import br.com.niceshoes.model.Produto;
import br.com.niceshoes.repository.ProdutoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
@Configuration
@Profile("demo")
public class CatalogoDemo {
    @Bean CommandLineRunner popularCatalogo(ProdutoRepository repository) {
        return args -> {
            if (repository.count() != 0) return;
            inserir(repository, "Nike Air Force 1 ’07", "699.90");
            inserir(repository, "adidas Samba OG", "599.90");
            inserir(repository, "New Balance 574", "549.90");
            inserir(repository, "ASICS GEL-1130", "599.90");
            inserir(repository, "Nike Pegasus 41", "799.90");
            inserir(repository, "adidas Ultraboost 5", "899.90");
            inserir(repository, "New Balance 530", "649.90");
            inserir(repository, "ASICS GEL-Kayano 31", "999.90");
            inserir(repository, "Nike Metcon 9", "749.90");
        };
    }
    private void inserir(ProdutoRepository repository, String nome, String preco) {
        repository.save(new Produto(nome, new BigDecimal(preco), true));
    }
}
