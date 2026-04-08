Sistema de Agendamentos (API REST)

API desenvolvida em Java com Spring Boot para gerenciar agendamentos de serviços.

Como executar o projeto
Clonar o repositório:
git clone [URL_DO_SEU_REPOSITORIO](https://github.com/Wellinton98/Explorando-APIs.git)

Abrir o projeto na IDE (VS Code)

Executar o projeto:
mvn spring-boot:run

A API estará disponível em:
http://localhost:8080

Banco H2:
http://localhost:8080/h2-console


Funcionalidades
Criar agendamento
Listar agendamentos
Buscar agendamento por ID
Atualizar agendamento
Deletar agendamento
Cancelar agendamento
Concluir agendamento

Rotas da API
Criar agendamento
POST /agendamentos

Listar todos
GET /agendamentos

Buscar por ID
GET /agendamentos/{id}

Atualizar
PUT /agendamentos/{id}

Deletar
DELETE /agendamentos/{id}

Cancelar
PATCH /agendamentos/{id}/cancelar

Concluir
PATCH /agendamentos/{id}/concluir

Exemplo de requisição (POST /agendamentos)
{
  "data": "2026-04-10",
  "horario": "14:00",
  "clienteNome": "Maria",
  "clienteTelefone": "999999999",
  "servicoNome": "Corte de cabelo",
  "profissionalNome": "João",
  "observacao": "Cliente preferencial",
  "valorServico": 50.0,
  "clienteId": 1,
  "servicoId": 1
}


Regras de negócio
Campos obrigatórios devem ser preenchidos
Não é permitido agendar no passado
Só é permitido agendar até 1 ano no futuro
Horário permitido: das 08:00 às 20:00
Não é permitido agendar em horário já ocupado
Não é possível cancelar um agendamento já concluído
Não é possível concluir um agendamento cancelado


Status do agendamento
AGENDADO
CANCELADO
CONCLUIDO


Banco de dados
Banco em memória: H2
Os dados são apagados ao reiniciar a aplicação

Criadores

Maira, Wellinton, Mateus