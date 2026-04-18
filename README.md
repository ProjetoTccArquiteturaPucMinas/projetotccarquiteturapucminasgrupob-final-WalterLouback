# Sistema de Cálculo de Descontos para Marketplace 
 
## Seu Nome: Walter Roberto

## Visão Geral do Projeto

O Marketplace Discount Calculator é uma aplicação web simples desenvolvida em Java com Spring Boot, cujo objetivo é simular um pequeno marketplace de acessórios para celulares e calcular descontos aplicados a um carrinho de compras.


O sistema foi projetado para fins acadêmicos, servindo como base para análise e comparação de arquiteturas de software. Com separação entre domínio, casos de uso, infraestrutura e apresentação.

A aplicação permite visualizar produtos cadastrados localmente em memória, selecionar itens para compor um carrinho e gerar um resumo da compra contendo subtotal, percentual de desconto, valor descontado e total final.

As regras de negócio consideram desconto por quantidade de itens, desconto por categoria de produto e um limite máximo de desconto acumulado. O projeto também inclui testes unitários para validar o comportamento esperado da funcionalidade principal.

Além do uso prático em Spring Boot, este sistema foi estruturado para apoiar atividades experimentais de implementação e compreensão arquitetural.

## Tecnologias

Java 17  
Spring Boot 3  
Thymeleaf  
JUnit 5  
Maven

## Como executar

mvn clean install  
mvn spring-boot:run

Acesse:

http://localhost:8080

## Como executar os testes

mvn test

## Resultado esperado no experimento

Inicialmente, parte dos testes falhará, porque a lógica de desconto ainda não foi implementada. O objetivo do participante é localizar o ponto correto da modificação, implementar a feature e fazer todos os testes passarem. Realizar um teste de ponta a ponta para concretizar todas as regras de negocios.

## Diagrama de arquitetura

Os arquivos do diagrama estão em docs/.

## Regras de negocios e cenarios de testes

Os arquivos estão em docs/.

## Responda o formulario de feedback (OBRIGATÓRIO)

Após a conclusão da atividade, por favor, responda o formulário de feedback para ajudar na avaliação do experimento:

https://docs.google.com/forms/d/e/1FAIpQLSfPDvalKaV3Nu-wuTGbQdNIkmqQMel4oyDVt0BhbDf9fjgdhw/viewform?usp=sharing&ouid=116206215759253976319
