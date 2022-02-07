# Onboard Experience API

A API para um projeto cujo a proposta é "gamificar" o processo de onboarding das empresas.

## Tecnologias e ferramentas

- Spring Boot V2.5.1
- Lombok V1.18.20
- Flyway V7.7.3
- PostgreSQL V12.3.+
- Maven V3.6.3
- MapStruct V1.4.2
- OpenAPI V1.5.9

## Variáveis de ambiente

Para rodar o projeto, é necessário possuir um arquivo .env dentro do diretório da aplicação.

`DATABASE_NAME`

`DATABASE_USER`

`DATABASE_PASSWORD`

`DATABASE_PORT`

`ENV`

`APP_PORT`

## Rodando localmente

### Utilizando uma IDE

Abra o Powershell (ou bash, em ambiente Linux) em modo administrador e clone o projeto para sua máquina.

```bash
  git clone git@github.com:WorkDB-Data-Business/onboard-exp-api.git
```

Vá para o diretório do projeto.

```bash
  cd onboard-exp-api
```

Instale as variáveis de ambiente.

```bash
  make install-env-variables-windows // ambiente Windows
  make install-env-variables-linux // ambiente Linux
```

É também possível retirar as variáveis de ambiente utilizando o comando abaixo.

```bash
  make remove-env-variables-windows // ambiente Windows
  make remove-env-variables-linux // ambiente Linux
```

Após definir as variáveis de ambiente, **reinicie a máquina**. Este passo é importante para que a IDE reconheça as variáveis de ambiente definidas.

Após voltar para o diretório da aplicação, instale as dependências e compile o projeto.

```bash
  make install
```

Após os procedimentos acima, é só abrir na IDE de sua preferência e executar o projeto.

### Utilizando Docker

Abra o Powershell e clone o projeto para sua máquina.

```bash
  git clone git@github.com:WorkDB-Data-Business/onboard-exp-api.git
```

Vá para o diretório do projeto.

```bash
  cd onboard-exp-api
```

Instale as dependências do projeto.

```bash
  make install
```

E então, execute o comando abaixo. O procedimento irá limpar as imagens e seus respectivos volumes, então irá recriar (caso exista) as imagens seguindo as instruções contidas no docker-compose.yml, e após isso, subirá as imagens. Esse processo não exige a execução do script das variáveis de ambiente, tampouco a reinicialização da máquina, porém ainda é necessário possuir o arquivo .env no diretório da aplicação.

```bash
  make run
```

## FAQ

#### O docker está buildando a imagem errada. O que fazer?

Primeiro, verifique se as instruções no docker-compose.yml e no Dockerfile estão corretas. Se sim, então utilize o comando abaixo e realize o build novamente.

```bash
    docker-compose down -v --rmi all
```

## Documentação da API

Ao subir a aplicação, é possível acessar o Swagger através do link abaixo. A porta da aplicação é definida no arquivo .env.

```bash
    http://localhost:8080/api/swagger-ui.html
```

## Gerenciamento dos containers

Ao subir a aplicação, é possível gerenciar os containers através do link abaixo.

```bash
    localhost:9000
```

## Observações

- **É necessário se atentar à versão do Maven utilizada no projeto.** Versões superiores à **3.6.3** ocorre uma quebra de compatibilidade devido a mudanças nas requisições HTTP/HTTPS aos repositórios.
