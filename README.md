# API de Autenticação e Gerenciamento

Esta é uma API de autenticação e gerenciamento de usuários, clientes, tarefas e equipes. Ela fornece funcionalidades para registrar usuários, fazer login, gerenciar clientes, criar e gerenciar tarefas, bem como criar e gerenciar equipes.

## Endpoints da API

### Autenticação

- **Registrar um novo usuário**
  - `POST /api/auth/register`
  - Registra um novo usuário na plataforma.

- **Login**
  - `POST /api/auth/login`
  - Permite que os usuários façam login na plataforma.

- **Verificar e-mail**
  - `POST /api/auth/verify`
  - Permite verificar o e-mail do usuário.

- **Obter informações do usuário logado**
  - `GET /api/auth/me`
  - Recupera informações do usuário logado.

### Clientes

- **Obter todos os clientes**
  - `GET /api/clients`
  - Retorna a lista de todos os clientes.

- **Registrar um novo cliente**
  - `POST /api/clients/create`
  - Registra um novo cliente na plataforma.

- **Buscar cliente por ID**
  - `GET /api/clients/{clientId}`
  - Retorna as informações de um cliente específico com base no ID.

- **Deletar cliente**
  - `DELETE /api/clients/{clientId}`
  - Deleta um cliente com base no ID.

- **Atualizar cliente**
  - `PUT /api/clients/{clientId}`
  - Atualiza as informações de um cliente com base no ID.

### Tarefas

- **Obter todas as tarefas**
  - `GET /api/tasks`
  - Retorna a lista de todas as tarefas.

- **Obter todas as tarefas de um usuário**
  - `GET /api/tasks/user-tasks`
  - Retorna a lista de todas as tarefas de um usuário específico.

- **Criar uma nova tarefa**
  - `POST /api/tasks/create`
  - Cria uma nova tarefa na plataforma.

- **Editar uma tarefa**
  - `PUT /api/tasks/update/{taskId}`
  - Edita as informações de uma tarefa com base no ID.

- **Buscar tarefa por ID**
  - `GET /api/tasks/search/{taskId}`
  - Retorna as informações de uma tarefa específica com base no ID.

- **Deletar tarefa**
  - `DELETE /api/tasks/{taskId}`
  - Deleta uma tarefa com base no ID.

### Equipes

- **Obter todas as equipes**
  - `GET /api/teams`
  - Retorna a lista de todas as equipes.

- **Criar uma nova equipe**
  - `POST /api/teams/create`
  - Cria uma nova equipe na plataforma.

- **Deletar equipe**
  - `DELETE /api/teams/{teamId}`
  - Deleta uma equipe com base no ID.

- **Editar equipe**
  - `PUT /api/teams/{teamId}`
  - Edita as informações de uma equipe com base no ID.

- **Adicionar usuário a uma equipe**
  - `POST /api/teams/{teamId}/addUser/{userId}`
  - Adiciona um usuário a uma equipe.

- **Remover usuário de uma equipe**
  - `DELETE /api/teams/{teamId}/removeUser/{userId}`
  - Remove um usuário de uma equipe.

