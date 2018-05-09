[![Build Status](https://travis-ci.org/vitoralves/springAPI.svg?branch=master)](https://travis-ci.org/vitoralves/springAPI)

# Spring Boot API

Projeto criado baseado na API desenvolvida utlizando Jersey para o projeto Spako disponível em (https://github.com/vitoralves/spako-api) mas agora explorando os recursos do framework Spring Boot e Java 8.

Base de dados Postgresql utilizando JPA e Spring Data JPA, testes unitários e de integração escrito com Junit e Mockito.

# Instalação

Projeto desenvolvido utilizando a IDE Spring Tool Suite. Clone o projeto para um repositório local, abra-o no STS para que todas dependências sejam instaladas, ao rodar o projeto o flyway se encarregará de criar a base de dados.

Também é possível rodar o projeto através do comando mvn spring-boot:run ou importá-lo para o STS executando mvn eclipse:eclipse.


# Outras tecnologias

Integração contínua com Travis

Versionamento da base com Flyway

Base embutida para testes utilizando H2

Autenticação e permissões com JWT

Swagger

EhCache

#Exemplo

Projeto rodando no Heroku, para exemplo o Swagger está habilitado em produção e pode ser acesso pela URI https://springboot-api.herokuapp.com/swagger-ui.html
