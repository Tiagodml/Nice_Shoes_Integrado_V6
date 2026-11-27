package br.com.niceshoes.controller;
import br.com.niceshoes.model.Cliente;
import br.com.niceshoes.repository.ClienteRepository;
import br.com.niceshoes.repository.PedidoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repository;
    private final PedidoRepository pedidos;
    public ClienteController(ClienteRepository repository, PedidoRepository pedidos) {
        this.repository = repository;
        this.pedidos = pedidos;
    }
    public record Dados(@NotBlank @Size(max = 120) String nome, @NotNull LocalDate clienteDesde) {}
    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Dados dados) {
        Cliente item = repository.save(new Cliente(dados.nome(), dados.clienteDesde()));
        return ResponseEntity.created(URI.create("/clientes/" + item.getId())).body(item);
    }
    @GetMapping
    public List<Cliente> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Cliente consultar(@PathVariable Long id) { return buscar(id); }
    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody Dados dados) {
        Cliente item = buscar(id);
        item.setNome(dados.nome());
        item.setClienteDesde(dados.clienteDesde());
        return repository.save(item);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        Cliente item = buscar(id);
        if (pedidos.existsByCliente_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Existem pedidos vinculados.");
        }
        repository.delete(item);
        return ResponseEntity.noContent().build();
    }
    private Cliente buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }
}
