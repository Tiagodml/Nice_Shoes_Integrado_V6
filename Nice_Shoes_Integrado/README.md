# Nice Shoes — API REST + loja integrada

Projeto acadêmico de Desenvolvimento Web Back-End: Java 17+, Spring Boot, Spring Data JPA, MySQL, H2, JSON e MVC (model, repository, controller). A loja e o painel são servidos pelo próprio Spring Boot; não abra o index.html por duplo clique. Não há cadastro simulado no front-end: clientes, produtos e pedidos são lidos e gravados via REST. Carrinho, favoritos e seleção de cliente são locais. A numeração BR (35–42) é obrigatória nos novos pedidos e persistida pela API. Não há autenticação nem pagamento real; uso acadêmico local, não expor publicamente sem segurança.

## 1. Executar no Windows

Instale JDK 17+ e Maven 3.6.3+; confirme `java -version` e `mvn -version` em um terminal novo. A IDE IntelliJ também pode importar o pom.xml. A primeira execução exige internet para resolver dependências Maven.

### Opção A — MySQL (configuração principal)

Com Docker Desktop instalado e em execução, abra o terminal na pasta do pom.xml:

```sh
docker compose up -d
docker compose ps
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

Aguarde o MySQL ficar healthy antes de iniciar Spring Boot. As credenciais em compose.yaml são apenas para desenvolvimento local. Se usar MySQL existente, configure DB_URL, DB_USER e DB_PASSWORD de acordo com sua instalação. O perfil `demo` insere nove produtos de exemplo somente se a tabela de produtos estiver vazia; não cria clientes ou pedidos.

### Opção B — H2 persistente (mais fácil, permitido no enunciado)

Sem Docker ou MySQL, execute `INICIAR_H2.cmd` ou:

```sh
mvn spring-boot:run -Dspring-boot.run.profiles=local,demo
```

O H2 relacional mantém os dados em `data/`. Se usar H2 nos testes do Postman, altere o relatório para indicar H2 como banco utilizado. O perfil `h2` separado usa memória e é destinado aos testes automatizados.

Abra http://localhost:8080 no navegador quando o terminal indicar que a aplicação iniciou. A barra da loja deve exibir “API Java conectada”. Clique em Área administrativa para gerenciar os registros. Para encerrar, Ctrl+C no terminal.

## 2. Testes

Em outro terminal, execute `mvn test` (testes MockMvc + Spring Data JPA + H2). A coleção do Postman está em `postman/Nice-Shoes.postman_collection.json`. Importe-a, confirme nomeAlunoRU = Tiago5580247, e envie cada requisição individualmente na ordem numerada. A variável baseUrl é http://localhost:8080. Os POST guardam IDs nas variáveis da coleção. Capture a tela após cada requisição: método, URL, status, corpo de envio quando aplicável e resposta. Para DELETE 204, a resposta sem corpo está correta.

- 01–03: POST cliente, produto, pedido (201).
- 04–06: GET geral das três entidades (200).
- 07–09: GET por ID (200).
- 10–12: PUT das três entidades (200, complementares).
- 13–15: DELETE pedido, produto, cliente (204). A ordem respeita as chaves estrangeiras.

Cenário obrigatório: Tiago5580247 compra duas unidades do Tênis Casual Nice Shoes, tamanho BR 39, por R$ 129,90 cada. A coleção cria o produto do estudo de caso independentemente do catálogo demo. Confira o RU antes de entregar. A loja poderá mostrar esse produto sem foto específica, pois a entidade Produto não tem campo de imagem.

## Atualização V6 — numeração

Pare o servidor V5 (Ctrl+C). Copie os arquivos da V6 sobre a pasta atual, preservando `data/` e `.git/`, ou extraia em nova pasta e copie `data/` com o servidor parado. O Hibernate adiciona a coluna `tamanho` automaticamente. Pedidos anteriores sem numeração continuam com tamanho nulo (não informado); ao editar, selecione o tamanho correto. O estoque continua Boolean por produto, sem controle separado de estoque por tamanho. O carrinho antigo sem tamanhos não é reutilizado na V6; escolha os produtos e tamanhos novamente. Importe a coleção Postman atualizada e execute `mvn test` para validar as mudanças.

## 3. Publicação e PDF

Crie um repositório vazio `nice-shoes` na sua conta GitHub. Na pasta do pom.xml, execute:

```sh
git init
git add .
git commit -m "API REST e loja Nice Shoes integrada"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/nice-shoes.git
git push -u origin main
```

Substitua SEU_USUARIO e autentique-se no GitHub. Inclua no PDF acadêmico o link real e os prints reais do Postman. Não publique o banco local nem credenciais pessoais. O documento anterior continua preliminar até anexar as evidências.

## 4. Estrutura

- `src/main/java/br/com/niceshoes/model`: Cliente, Produto, Pedido.
- `src/main/java/br/com/niceshoes/repository`: Spring Data JPA.
- `src/main/java/br/com/niceshoes/controller`: 15 endpoints e erros JSON.
- `src/main/resources/static`: loja, painel administrativo, cliente REST e JavaScript.
- `src/test`: teste de integração Spring Boot / H2.
- `postman`: coleção de 15 requisições.
- `docs`: diagrama e registro de validação.

O catálogo usa fotos externas de referência, cuja disponibilidade não foi confirmada. A opção de importar fotos na loja funciona na sessão atual. Os preços são demonstrativos. A interface não é requisito da atividade; o essencial é comprovar a API em funcionamento.
