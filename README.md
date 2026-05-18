# Outsera Challenge

API desenvolvida em Spring Boot para importação e processamento de dados do arquivo `movielist.csv`, utilizando banco em memória H2.

---

## Tecnologias

- Java 21
- Gradle
- Spring Boot
- Spring Data JPA
- H2 Database
- JUnit 5

---

## Como executar o projeto

### Pré-requisitos

- Java 21+
- Gradle 8+

---

### Executando a aplicação

```bash
./gradlew bootRun
```

A aplicação iniciará automaticamente e realizará a importação do arquivo:

```text
src/main/resources/movielist.csv
```

para o banco H2 em memória.

---

## Endpoint disponível

Após iniciar a aplicação, o seguinte endpoint estará disponível:

### GET `/award-interval`

```text
http://localhost:8080/award-interval
```

O endpoint retorna:

- produtores com menor intervalo entre premiações consecutivas
- produtores com maior intervalo entre premiações consecutivas

### Exemplo de resposta

```json
{
  "min": [
    {
      "producer": "Producer Name",
      "interval": 1,
      "previousWin": 2008,
      "followingWin": 2009
    }
  ],
  "max": [
    {
      "producer": "Another Producer",
      "interval": 13,
      "previousWin": 2000,
      "followingWin": 2013
    }
  ]
}
```

---

## Como executar os testes

```bash
./gradlew test
```

---

## Sobre o arquivo movielist.csv

O projeto utiliza o arquivo abaixo como fonte de dados inicial da aplicação:

```text
src/main/resources/movielist.csv
```

Durante a inicialização da aplicação:

1. O arquivo CSV é lido automaticamente
2. Os dados são convertidos para entidades do domínio
3. Os registros são persistidos no banco H2 em memória

---

## Estrutura esperada do CSV

O arquivo deve conter os headers:

```csv
year;title;studios;producers;winner
```

Exemplo:

```csv
1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes
1981;Mommie Dearest;Paramount Pictures;Frank Yablans;
```

---

## Observações importantes

- O banco H2 é recriado a cada execução da aplicação
- Producers e Studios são deduplicados automaticamente durante a importação
- O carregamento do CSV ocorre automaticamente no startup da aplicação

---

## Console H2

Disponível em:

```text
http://localhost:8080/h2-console
```

### JDBC URL

```text
jdbc:h2:mem:testdb
```

### User

```text
sa
```

### Password

```text
Vazio - sem senha
```