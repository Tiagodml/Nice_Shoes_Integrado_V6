package br.com.niceshoes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class NiceShoesIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Test void arquivosDaInterfaceDisponiveis() throws Exception {
        mvc.perform(get("/index.html")).andExpect(status().isOk());
        mvc.perform(get("/admin.html")).andExpect(status().isOk());
        mvc.perform(get("/api.js")).andExpect(status().isOk());
    }
    @Test void crudDasTresEntidades() throws Exception {
        String cliente = "{\"nome\":\"Tiago5580247\",\"clienteDesde\":\"2026-11-26\"}";
        String produto = "{\"nome\":\"Tênis Casual Nice Shoes\",\"preco\":129.90,\"estoque\":true}";
        long c = criar("/clientes", cliente);
        long p = criar("/produtos", produto);
        String pedido = "{\"clienteId\":"+c+",\"produtoId\":"+p+",\"quantidade\":2,\"tamanho\":39}";
        long o = criar("/pedidos", pedido);
        mvc.perform(get("/pedidos/"+o)).andExpect(status().isOk()).andExpect(jsonPath("$.tamanho").value(39));
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido.replace("39}","39.5}"))).andExpect(status().isBadRequest());
        for (int tamanho : new int[]{34,43}) {
            String invalido = pedido.replace("39}", tamanho+"}");
            mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(invalido)).andExpect(status().isBadRequest());
            mvc.perform(put("/pedidos/"+o).contentType(MediaType.APPLICATION_JSON).content(invalido)).andExpect(status().isBadRequest());
        }
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido.replace(",\"tamanho\":39", ""))).andExpect(status().isBadRequest());
        mvc.perform(put("/pedidos/"+o).contentType(MediaType.APPLICATION_JSON).content(pedido.replace("39}","42}"))).andExpect(status().isOk()).andExpect(jsonPath("$.tamanho").value(42));
        String[] rotas = {"/clientes", "/produtos", "/pedidos"};
        long[] ids = {c,p,o};
        String[] corpos = {cliente,produto,pedido};
        for (int i=0; i<rotas.length; i++) {
            mvc.perform(get(rotas[i])).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
            mvc.perform(get(rotas[i]+"/"+ids[i])).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(ids[i]));
            mvc.perform(put(rotas[i]+"/"+ids[i]).contentType(MediaType.APPLICATION_JSON).content(corpos[i])).andExpect(status().isOk());
        }
        mvc.perform(delete("/clientes/"+c)).andExpect(status().isConflict());
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON).content(pedido.replace("\"quantidade\":2", "\"quantidade\":0"))).andExpect(status().isBadRequest());
        for (int i=2; i>=0; i--) {
            mvc.perform(delete(rotas[i]+"/"+ids[i])).andExpect(status().isNoContent());
            mvc.perform(get(rotas[i]+"/"+ids[i])).andExpect(status().isNotFound());
        }
    }
    private long criar(String rota, String corpo) throws Exception {
        String resposta = mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON).content(corpo))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return json.readTree(resposta).get("id").asLong();
    }
}
