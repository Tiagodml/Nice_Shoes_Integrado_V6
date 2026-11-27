package br.com.niceshoes.controller;
import br.com.niceshoes.model.*;
import br.com.niceshoes.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    public PedidoController(PedidoRepository repository, ClienteRepository clientes, ProdutoRepository produtos) {
        this.repository = repository;
        this.clientes = clientes;
        this.produtos = produtos;
    }
    public record Dados(@NotNull @Positive Long clienteId, @NotNull @Positive Long produtoId, @NotNull @Positive Integer quantidade, @NotNull @Min(35) @Max(42) Integer tamanho) {}
    public record Resposta(Long id, Long clienteId, Long produtoId, Integer quantidade, Integer tamanho) {
        static Resposta de(Pedido p) {
            return new Resposta(p.getId(), p.getCliente().getId(), p.getProduto().getId(), p.getQuantidade(), p.getTamanho());
        }
    }
    @PostMapping
    public ResponseEntity<Resposta> criar(@Valid @RequestBody Dados dados) {
        Pedido pedido = repository.save(new Pedido(cliente(dados.clienteId()), produto(dados.produtoId()), dados.quantidade(), dados.tamanho()));
        return ResponseEntity.created(URI.create("/pedidos/" + pedido.getId())).body(Resposta.de(pedido));
    }
    @GetMapping
    public List<Resposta> listar() { return repository.findAll().stream().map(Resposta::de).toList(); }
    @GetMapping("/{id}")
    public Resposta consultar(@PathVariable Long id) { return Resposta.de(buscar(id)); }
    @PutMapping("/{id}")
    public Resposta atualizar(@PathVariable Long id, @Valid @RequestBody Dados dados) {
        Pedido pedido = buscar(id);
        pedido.setCliente(cliente(dados.clienteId()));
        pedido.setProduto(produto(dados.produtoId()));
        pedido.setQuantidade(dados.quantidade());
        pedido.setTamanho(dados.tamanho());
        return Resposta.de(repository.save(pedido));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        repository.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }
    private Pedido buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }
    private Cliente cliente(Long id) {
        return clientes.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }
    private Produto produto(Long id) {
        Produto produto = produtos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
        if (!Boolean.TRUE.equals(produto.getEstoque())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto indisponível.");
        return produto;
    }
}
