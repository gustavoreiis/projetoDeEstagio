# Paróquia Nossa Senhora de Lourdes

## Site da Paróquia Nossa Senhora de Lourdes

**Versão:** 1.0

# Pedidos do Investidor

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
2. [Estabelecer Perfil do Investidor ou do Usuário](#estabelecer-perfil-do-investidor-ou-do-usuário)  
3. [Avaliando o Problema](#avaliando-o-problema)  
   3.1 [Controle das Informações de Inscrições](#controle-das-informações-de-inscrições)  
   3.2 [Gerenciamento de Grupos](#gerenciamento-de-grupos)  
   3.3 [Controle de Presença](#controle-de-presença)
4. [Entendendo o Ambiente do Usuário](#entendendo-o-ambiente-do-usuário)  
5. [Recapitulação para Entendimento](#recapitulação-para-entendimento)       
6. [Entradas do Analista no Problema do Investidor](#entradas-do-analista-no-problema-do-investidor-validar-ou-invalidar-premissas)  
   5.1 [Inscrições](#inscrições)    
   5.2 [Grupos](#grupos)  
   5.3 [Presença](#presença) 
7. [Avaliando Sua Solução](#avaliando-sua-solução-se-aplicável)  
8. [Avaliando a Oportunidade](#avaliando-a-oportunidade)  
9. [Avaliando a Confiabilidade, o Desempenho e as Necessidades de Suporte](#avaliando-a-confiabilidade-o-desempenho-e-as-necessidades-de-suporte)  
   9.1 [Outros Requisitos](#outros-requisitos)  
10. [Wrap-Up](#wrap-up)  
11. [Resumo do Analista](#resumo-do-analista)  

---

# Introdução

O documento fornece o entendimento dos pedidos do investidor, incluindo o escopo, objetivo, definições e a visão geral do documento. A entrevista determina as necessidades e os requisitos funcionais e não funcionais do investidor. A partir das informações obtidas por meio dessa ação, serão conhecidas as soluções para resolução dos problemas encontrados em cada situação.

---

## Objetivo

O objetivo do documento é realizar o levantamento das informações e requisitos baseados na necessidade do usuário. Portanto, é realizada uma entrevista para compreender os desafios e problemas encontrados pelo investidor, a fim de destacar os requisitos funcionais e não funcionais do produto, para que ao iniciar o desenvolvimento do sistema, a implementação esteja de acordo com a necessidade.

---

## Escopo

O pedido do investidor está relacionado à Paróquia Nossa Senhora de Lourdes e a organização dos encontros, retiros e pessoas do grupo de oração da paróquia. A coleta das informações será realizada por meio de uma entrevista com os integrantes da organização do grupo.

---

## Definições, Acrônimos e Abreviações

Consulte o documento Glossário.

---

## Referências

RUP (Rational Unified Process) e documento de Glossário.

---

## Visão Geral

O documento é elaborado para apresentar o perfil e situação do investidor, identificando o ambiente de atuação, problemas e desafios encontrados e as possíveis soluções que serão aplicadas no produto a fim de atender as necessidades do investidor com base nos requisitos que serão obtidos e documentados por meio da entrevista.

---

# Estabelecer Perfil do Investidor ou do Usuário

| Pergunta | Resposta |
|----------|---------|
| **Nome:** | Samuel Acioli |
| **Empresa / Segmento de Mercado:** | Paróquia Nossa Senhora de Lourdes |
| **Cargo:** | Coordenador |
| **Responsabilidades principais:** | Gerenciamento de pessoas e retiros |
| **Distribuíveis produzidos e destinatários:** | Organizar eventos, controlar pessoas |
| **Critérios de sucesso:** | Quantidade de participantes do grupo |
| **Problemas enfrentados:** | Desorganização, grande quantidade de informações em planilhas, processos manuais |
| **Tendências que impactam o trabalho:** | O controle dos dados por meio de planilhas dificultam a manipulação dos dados. Além disso, o recebimento de informações por meio de fichas impressas também tornam o trabalho manual, o que demanda tempo para a execução das atividades. |

---

# Avaliando o Problema

- Para quais problemas faltam boas soluções?  
Controle das Informações de Inscrições; Gerenciamento de Grupos; Controle de Presença.  

### Controle das Informações de Inscrições
- Descrição detalhada do problema:  
   As inscrições para os encontros ou retiros são realizadas por meio de fichas impressas, após o preenchimento, os dados são transpostos para planilhas, gerando um grande volume de dados e dificultando a manipulação.
- Justificativas e soluções propostas:  
   A partir do entendimento desse problema, acredita-se que fornecer aos participantes a opção de realizar a inscrição por meio de um site, armazenando todos os dados de forma organizada, seria uma possível solução para a resolução do problema em questão.

### Gerenciamento de Grupos
- Descrição detalhada do problema:  
   Para a realização do encontro ou retiro, devem ser organizados grupos de pessoas, tanto para equipes quanto para a disposição de pessoas em quartos nas casas de encontro. Esse controle também é realizado em planilhas e constantemente deve ser editado para a realização de alterações na disposição dos grupos.
- Justificativas e soluções propostas:  
   Para a resolução desse problema, seria ideal a organização dos grupos por meio das informações dos participantes já inscritos no evento.

### Controle de presença
- Descrição detalhada do problema:
  Para algumas formações do grupo são realizadas chamadas para controlar a presença dos participantes. Todas as presenças e faltas são contabilizadas, pois é necessário a constância do participante para que tenha a permissão de participar de determinados encontros. Esse controle é realizado somente por listas impressas e planilhas.
- Justificativas e soluções propostas:  
  Com o objetivo de facilitar todo esse processo de controle de presença, uma possível solução seria uma funcionalidade no sistema para a realização das chamadas, juntamente com um monitoramento caso os participantes ultrapassem o limite de faltas.

---

# Entendendo o Ambiente do Usuário

- **Quem são os usuários?** Coordenadores, e participantes do grupo de oração.  
- **Qual é o seu nível educacional e experiência com computadores?** Ensino Médio Completo. Experiência com computadores intermediária.  
- **Eles já usaram esse tipo de aplicativo antes?** Usuários habituados a tecnologias semelhantes ao sistema proposto.  
- **Quais plataformas estão em uso e quais são os planos futuros?** Ferramentas Google e planilhas.   
- **Quais são suas expectativas quanto à utilidade e tempo de treinamento?** Solução que facilite o processo das ações realizadas com curto tempo de treinamento.  
- **Que tipo de documentação você precisa?** Necessário um documento com a visão geral do sistema, para que seja possível a compreensão das funcionalidades.  

---

# Recapitulação para Entendimento

Você mencionou os seguintes problemas:

1. organização das informações recebidas por meio das inscrições dos participantes;  
2. Processo de inscrição e pagamento;  
3. Gerenciar grupos para a disposição em encontros; 
4. Realizar um controle de presença, por meio de chamadas e relatórios. 

Isso representa corretamente suas dificuldades com a solução existente? Sim  

---

# Entradas do Analista no Problema do Investidor (Validar ou Invalidar Premissas)
 
- ### Inscrições:
  - **Esse problema é real?** Sim 
  - **O que o causa?** Grande volume de informações mal organizadas.  
  - **Como é resolvido atualmente?** Por meio de planilhas online. 
  - **Como gostaria que fosse resolvido?** Por meio de um site que facilite o gerenciamento dos dados.  
  - **Quão prioritário é comparado aos outros problemas?** Maior Prioridade.

- ### Grupos:  
  - **Esse problema é real?** Sim 
  - **O que o causa?** Controle de grupos que frequentemente podem ser alterados. 
  - **Como é resolvido atualmente?** Por meio de planilhas online. 
  - **Como gostaria que fosse resolvido?** Por meio de um site que facilite o gerenciamento dos dados.   
  - **Quão prioritário é comparado aos outros problemas?** Baixa prioridade

- ### Presença: 
  - **Esse problema é real?** Sim 
  - **O que o causa?** É necessário realizar a presença para permitir a participação nos encontros. Esse processo é realizado manualmente com ferramentas que dificultam a ação.
  - **Como é resolvido atualmente?** Chamadas impressas. 
  - **Como gostaria que fosse resolvido?** Por meio de um site que controlasse a presença dos participantes e gerasse as informações dos participantes que não possuem a frequência esperada.   
  - **Quão prioritário é comparado aos outros problemas?** Média prioridade

---

# Avaliando Sua Solução

- Um site capaz de gerenciar todo o fluxo de inscrição para os eventos realizadas no grupo, incluindo a inscrição e controle das informações gerais dos participantes. Permitindo, também, organizar os grupos que irão ser necessários dentro do evento, tanto para equipes, quanto para quartos. Além disso, controlar as presenças dos participantes que fizeram um encontro, para que assim, seja possível conhecer a frequência de cada um.

**Qual seria a importância desses recursos para você?** Alta importância, facilitando o processo das atividades que são realizadas. 

---

# Avaliando a Oportunidade

- **Necessidade do produto na organização:** Produto seria fundamental para a execução das atividades, considerando que, atualmente, são realizadas de forma manual e trabalhosa.   
- **Número de usuários previstos:** Coordenadores, colaboradores e participantes, resultando em média 150 usuários.  
- **Critérios para uma solução bem-sucedida:** A solução será considerada bem-sucedida, caso atenda as funcionalidades solicitadas com sucesso, facilitando o processo e a realização das atividades.  

---

# Avaliando a Confiabilidade, o Desempenho e as Necessidades de Suporte

- **Expectativas de confiabilidade?** Site seguro que não gere problemas ao manipular as informações.  
- **Expectativas de desempenho?** Alto desempenho, sem lentidões ao realizar as ações no site. 
- **Necessidades de suporte e manutenção?** É necessário suporte para auxílio dos possíveis problemas na utilização.    
- **Requisitos de segurança?** Segurança dos dados pessoais dos inscritos, é necessário que somente os coordenadores tenham acesso. 
- **Requisitos de instalação, configuração e licença?** Ter acesso a internet.  

---

## Outros Requisitos

- **Existem requisitos regulatórios ou ambientais que precisam ser seguidos?**   É necessário segurança para a realização do pagamento das inscrições. 

---

# Wrap-Up

- Há mais alguma pergunta que deveríamos ter feito? Não.  
- Podemos entrar em contato caso surjam novas dúvidas? Sim.  
- Você estaria disposto a revisar os requisitos conosco? Sim. 

---

# Resumo do Analista

Os **três problemas prioritários** identificados foram:

1. **Inscrições:** Controle do processo de inscrição, dados, informações.
2. **Grupos:** Organização de grupos para os eventos.
3. **Presença:** Controle de presença dos participantes do grupo de oração. 

---

**Versão:** 1.0  
**Data:** 01/03/2025  
**Empresa:** Paróquia Nossa Senhora de Lourdes  
**Confidencialidade:** Confidencial  
**Copyright:** © Paróquia Nossa Senhora de Lourdes 2025 