# Paróquia Nossa Senhora de Lourdes

## Site da Paróquia Nossa Senhora de Lourdes

**Versão:** 1.0

# Especificação Complementar

**Data:** 01/03/2025

---

## Histórico da Revisão

| Data | Versão | Descrição | Autor |
|------|--------|-----------|--------|
| 01/03/2025 | 1.0 | Primeira versão do documento | Gustavo Martins dos Reis |

---

## Índice

1. [Introdução](#introdução)  
   1.1 [Objetivo](#objetivo)  
   1.2 [Escopo](#escopo)  
   1.3 [Definições, Acrônimos e Abreviações](#definições-acrônimos-e-abreviações)  
   1.4 [Referências](#referências)  
   1.5 [Visão Geral](#visão-geral)  
2. [Funcionalidade](#funcionalidade)  
   2.1 [Controle do Fluxo de Inscrição](#controle-do-fluxo-de-inscrição)  
   2.2 [Gerenciamento de Grupos](#gerenciamento-de-grupos)  
   2.3 [Controle de Presença](#controle-de-presença) 
3. [Utilidade](#utilidade)  
   3.1 [Tempo de Treinamento](#tempo-de-treinamento)  
   3.2 [Tempo de Realização das Tarefas](#tempo-de-realização-das-tarefas)   
   3.3 [Utilização do Sistema](#utilização-do-sistema)       
4. [Confiabilidade](#confiabilidade)  
   4.1 [Disponibilidade](#disponibilidade)  
   4.2 [Erros](#erros)  
   4.3 [MTTR](#mttr)  
   4.4 [Exatidão](#exatidão)   
5. [Desempenho](#desempenho)  
   5.1 [Tempo de Resposta](#tempo-de-resposta)  
6. [Suportabilidade](#suportabilidade)  
   6.1 [Padrões de Codificação](#padrões-de-codificação)  
   6.2 [Convenções de Nomenclatura](#convenções-de-nomenclatura)  
   6.3 [Utilitários de Manutenção](#utilitários-de-manutenção) 
7. [Restrições de Design](#restrições-de-design)  
   7.1 [Design](#design)  
8. [Documentação do Usuário On-line e Requisitos do Sistema de Ajuda](#documentação-do-usuário-on-line-e-requisitos-do-sistema-de-ajuda)  
9. [Componentes Comprados](#componentes-comprados)  
10. [Interfaces](#interfaces)  
    10.1 [Interfaces com o Usuário](#interfaces-com-o-usuário)  
    10.2 [Interfaces de Hardware](#interfaces-de-hardware)  
    10.3 [Interfaces de Software](#interfaces-de-software)  
    10.4 [Interfaces de Comunicações](#interfaces-de-comunicações)  
11. [Requisitos de Licença](#requisitos-de-licença)  
12. [Observações Legais, sobre Direitos Autorais e Outras Observações](#observações-legais-sobre-direitos-autorais-e-outras-observações)  
13. [Padrões Aplicáveis](#padrões-aplicáveis)  

---

# Introdução

A Especificação Suplementar captura os requisitos do sistema que não são expressos nos casos de uso do modelo. Inclui requisitos legais, regulamentares, atributos de qualidade e restrições de design e os requisitos não funcionais do sistemas que são fundamentais para uma boa experiência do usuário ao utilizar o sistema.

---

## Objetivo

Na Especificação Complementar tem como objetivo detalhar todos os requisitos não funcionais do projeto, tais como requisitos de compatibilidade do sistema, questões de design, confiabilidade, suportabilidade e até requisitos legais e de regulamentação.

---

## Escopo

O documento de Especificação Complementar é destinado para documentar os requisitos não funcionais necessários para o desenvolvimento do sistema que será utilizado no grupo de Oração da Paróquia Nossa Senhora de Lourdes.

---

## Definições, Acrônimos e Abreviações

Consulte o documento Glossário

---

## Referências

RUP (Rational Unified Process) e documento de Glossário.

---

## Visão Geral

A Especificação Suplementar serão apresentados os requisitos não funcionais, como a utilidade, confiabilidade, desempenho, suportabilidade, questões de design, documentação de usuário, requisitos de licença para uso do sistema, questões legais e padrões aplicáveis.

---

# Funcionalidade

### Controle do Fluxo de Inscrição

Nesse requisito funcional, os participantes e colaboradores poderão realizar a inscrição, fornecendo todas as informações necessárias, incluindo o pagamento. Enquanto que os coordenadores, poderão gerenciar as informações dos participantes, além de auxiliar na inscrição, editando as informações quando necessário. A partir disso, o requisito inclui o controle de todas as informações de forma organizada, para que seja possível o acesso, de forma eficiente.

### Gerenciamento de Grupos

Com o cadastro dos participantes e colaboradores no encontro ou retiro, será possível criar grupos e equipes, adicionando e removendo pessoas, quando necessário.

### Controle de Presença

O sistema ofertará pautas para realizar chamadas de acordo com os participantes que fizeram o encontro. A partir das chamadas, será produzido um relatório com avisos e informações da frequência de cada pessoa.

---

# Utilidade

### Tempo de Treinamento

O tempo de treinamento deve ser curto.

### Tempo de Realização das Tarefas

O tempo necessário para a realização das tarefas não deve ser longo, considerando sua complexidade.

### Utilização do Sistema

O sistema deve ser de fácil utilização, com telas e design que contribua com o usuário na execução das tarefas.

---

# Confiabilidade

### Disponibilidade

O sistema deve estar disponível em tempo integral.

### Erros

Por gerenciar informações pessoais dos cadastrados, o sistema deve evitar a ocorrência de erros. Caso ocorram erros, os usuários devem ser notificados.

### MTTR

O tempo para correção de falhas (Mean Time to Repair) é de 1 dia.

### Exatidão

Por gerenciar as informações dos participantes dos eventos, incluindo dados pessoais e de pagamento, é necessário garantir a exatidão no processamento dos dados para que não haja duplicidade de dados ou outras falhas que tragam possíveis equívocos na manipulação das ações.

---

# Desempenho

[Inclua tempos de resposta, rendimento, capacidade, degradação e uso de recursos.]

### Tempo de Resposta

O sistema deve possuir um tempo de resposta médio de 5 segundos para proessamento de alguma ação.

---

# Suportabilidade

### Padrões de Codificação

O sistema utilizará arquitetura MVC com o *framework Spring*.

### Convenções de Nomenclatura

O sistema utilizará nomes em português aplicando os conceitos de Camel Case.

### Utilitários de Manutenção

Somente os coordenadores terão acesso de manutenção no sistema.

---

# Restrições de Design

### Design

O sistema será realizada no padrão Web, podendo ser acessados pelo principais navegadores utilizados.

---

# Documentação do Usuário On-line e Requisitos do Sistema de Ajuda

Para instrução aos usuários, poderão ser acessados os documentos criados a partir do modelo do RUP (Rational Unified Process).

---

# Componentes Comprados

Não será necessária a compra de componentes.

---

# Interfaces

## Interfaces com o Usuário

As telas serão projetadas de acordo com as ações do usuário, com o objetivo de ter a usabilidade necessária conforme as funcionalidades, tendo a necessidade do cadastro para identificação dos usuários.

## Interfaces de Hardware

O usuário deve utilizar uma máquina com acesso a internet.

## Interfaces de Software

O sistema será desenvolvido por meio do *framework* de Java, *Spring*, com banco de dados PostgreSQL.

## Interfaces de Comunicações

A comunicação do sistema será realizada por meio do protocolo HTTP (Protocolo de Transferência de Texto).

---

# Requisitos de Licença

Não se aplica.

---

# Observações Legais, sobre Direitos Autorais e Outras Observações

O sistema é propriedade da Paróquia Nossa Senhora de Lourdes.

---

# Padrões Aplicáveis

Os padrões aplicados no desenvolvimento desse sistema são desenvolvidos por meio do modelo de documentação RUP (Rational Unified Process) e nas diagramações UML.

---

**Versão:** 1.0  
**Data:** 01/03/2025  
**Empresa:** Paróquia Nossa Senhora de Lourdes  
**Confidencialidade:** Confidencial  
**Copyright:** © Paróquia Nossa Senhora de Lourdes 2025  