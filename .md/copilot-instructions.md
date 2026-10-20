\# Instruções do GitHub Copilot — DentalFlow



\## Objetivo



Desenvolver e manter o projeto DentalFlow respeitando a arquitetura, o código existente e o schema do banco de dados.



\## Documentação de referência



Antes de implementar qualquer funcionalidade, consulte:



\- `ARCHITECTURE\_REFERENCE.md`: estrutura e padrões arquiteturais do projeto.

\- `DATABASE\_REFERENCE.md`: tabelas, colunas, chaves e relacionamentos do banco.



Consulte também o código-fonte das funcionalidades existentes para confirmar como os padrões são aplicados na prática.



\## Regras obrigatórias



1\. Siga a arquitetura já utilizada no projeto. Não crie novas camadas, padrões ou abstrações sem necessidade.

2\. Reutilize classes, interfaces, DTOs, mappers, exceptions e convenções existentes quando forem aplicáveis.

3\. Não invente tabelas, colunas, métodos, classes, relacionamentos ou regras de negócio.

4\. Se houver informações insuficientes ou conflitos entre a documentação e o código, informe a dúvida antes de tomar decisões importantes.

5\. Implemente somente o que foi solicitado. Evite refatorações e alterações em arquivos não relacionados à tarefa.

6\. Utilize inglês nos nomes de classes, métodos, atributos, DTOs e endpoints, seguindo as convenções existentes. Os logs podem permanecer em português.

7\. Prefira soluções simples, compatíveis com o nível e as tecnologias já utilizados no projeto.



\## Implementação e validação



Trabalhe em uma rota ou funcionalidade por vez.



Antes de alterar o código, identifique uma implementação existente semelhante e siga seu padrão.



Ao concluir, execute os testes pertinentes, quando possível, e informe o que foi alterado, quais testes foram executados e se houve falhas.



Nunca afirme que os testes passaram sem ter executado e verificado seus resultados.

