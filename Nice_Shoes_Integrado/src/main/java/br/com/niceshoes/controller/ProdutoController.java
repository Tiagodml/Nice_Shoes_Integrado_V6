package br.com.niceshoes.controller;
import br.com.niceshoes.model.Produto;
import br.com.niceshoes.repository.ProdutoRepository;
import br.com.niceshoes.repository.PedidoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
import java.math.BigDecimal;
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;
    private final PedidoRepository pedidos;
    public ProdutoController(ProdutoRepository repository, PedidoRepository pedidos) {
        this.repository = repository;
        this.pedidos = pedidos;
    }
    public record Dados(@NotBlank @Size(max = 120) String nome, @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal preco, @NotNull Boolean estoque) {}
    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Dados dados) {
        Produto item = repository.save(new Produto(dados.nome(), dados.preco(), dados.estoque()));
        return ResponseEntity.created(URI.create("/produtos/" + item.getId())).body(item);
    }
    @GetMapping
    public List<Produto> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Produto consultar(@PathVariable Long id) { return buscar(id); }
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @Valid @RequestBody Dados dados) {
        Produto item = buscar(id);
        item.setNome(dados.nome());
        item.setPreco(dados.preco());
        item.setEstoque(dados.estoque());
        return repository.save(item);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        Produto item = buscar(id);
        if (pedidos.existsByProduto_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Existem pedidos vinculados.");
        }
        repository.delete(item);
        return ResponseEntity.noContent().build();
    }
    private Produto buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }
}
