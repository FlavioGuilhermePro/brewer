![Java](https://img.shields.io/badge/Java-17+-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)



# 🍺 Brewer - Sistema de Gestão de Cervejaria

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)


Sistema completo de gerenciamento para cervejarias, desenvolvido com Spring Boot, focado em controle de estoque, gestão de clientes e processamento de pedidos.

![Screenshot do Dashboard](docs/images/screenshot-dashboard.png)

## 📋 Sobre o Projeto

O **Brewer** é um sistema web desenvolvido para facilitar a gestão operacional de cervejarias, oferecendo funcionalidades essenciais como:

- ✅ Controle completo de estoque de cervejas
- ✅ Cadastro e gerenciamento de clientes
- ✅ Criação e acompanhamento de pedidos
- ✅ Dashboard com métricas em tempo real
- ✅ Sistema de autenticação seguro
- ✅ Interface responsiva e intuitiva

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.x**
  - Spring MVC
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **Hibernate** (ORM)
- **Lombok** (Redução de boilerplate)

### Frontend
- **Thymeleaf** (Template Engine)
- **HTML5 / CSS3**
- **JavaScript Vanilla**
- **Font Awesome** (Ícones)

### Banco de Dados
- **MySQL 8.0+**

### Ferramentas
- **Maven** (Gerenciamento de dependências)
- **Git** (Controle de versão)

## 📦 Funcionalidades Principais

### 1. Gestão de Estoque
- Cadastro de cervejas com SKU, nome, volume e preço
- Controle automático de quantidade em estoque
- Busca e filtros por nome
- Paginação de resultados
- Edição e exclusão de produtos

### 2. Gestão de Clientes
- Cadastro completo de clientes (nome, CPF, endereço)
- Listagem e edição de dados
- Criação de pedidos vinculados ao cliente

### 3. Sistema de Pedidos
- Criação de pedidos para clientes
- Adição de múltiplos itens ao pedido
- Cálculo automático de totais
- Controle de status do pedido:
  - `PENDENTE` → `CONFIRMADO` → `EM_TRANSITO` → `ENTREGUE`
- Redução automática de estoque ao finalizar pedido
- Devolução de estoque ao cancelar pedido
- **Histórico preservado**: mesmo que um produto seja removido do catálogo, os pedidos antigos mantêm os dados

### 4. Dashboard
- Faturamento do mês atual
- Quantidade de pedidos do dia
- Cerveja mais vendida
- Distribuição de pedidos por status
- Alertas de estoque baixo (< 10 unidades)

### 5. Segurança
- Autenticação com Spring Security
- Senhas criptografadas com BCrypt
- Proteção de rotas privadas
- Sessão gerenciada automaticamente

## 🏗️ Arquitetura do Projeto

```
brewer/
├── src/main/java/com/example/brewer/
│   ├── config/              # Configurações (Security, etc)
│   ├── controller/          # Controladores MVC
│   ├── model/              # Entidades JPA
│   ├── repository/         # Repositórios Spring Data
│   ├── service/            # Regras de negócio
│   └── BrewerApplication.java
│
├── src/main/resources/
│   ├── static/
│   │   ├── css/           # Estilos
│   │   ├── js/            # Scripts
│   │   └── images/        # Imagens e logos
│   ├── templates/         # Views Thymeleaf
│   └── application.properties
│
└── pom.xml                # Dependências Maven
```

## 💻 Instalação e Execução

### Pré-requisitos

- Java JDK 17 ou superior
- MySQL 8.0 ou superior
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Passo 1: Clone o Repositório

```bash
git clone https://github.com/seu-usuario/brewer.git
cd brewer
```

### Passo 2: Configure o Banco de Dados

Crie um banco de dados no MySQL:

```sql
CREATE DATABASE brewer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Passo 3: Configure o `application.properties`

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/brewer
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Thymeleaf
spring.thymeleaf.cache=false
```

### Passo 4: Execute o Projeto

**Via Maven:**
```bash
mvn spring-boot:run
```

**Via IDE:**
- Importe o projeto como Maven Project
- Execute a classe `BrewerApplication.java`

### Passo 5: Acesse o Sistema

Abra o navegador e acesse:
```
http://localhost:8080
```

**Credenciais padrão:**
- **Usuário:** `admin`
- **Senha:** `123456`

## 📊 Modelo de Dados

### Diagrama ER Simplificado

```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│  Cerveja    │       │ ItemPedido   │       │   Pedido    │
├─────────────┤       ├──────────────┤       ├─────────────┤
│ id          │◄──────│ cerveja_id   │       │ id          │
│ sku         │       │ pedido_id    │──────►│ cliente_id  │
│ nome        │       │ cerveja_nome │       │ data_pedido │
│ volume      │       │ quantidade   │       │ total       │
│ estoque     │       │ valor_unit   │       │ status      │
│ valor_venda │       │ valor_total  │       └─────────────┘
└─────────────┘       └──────────────┘              │
                                                     │
                                              ┌─────────────┐
                                              │  Cliente    │
                                              ├─────────────┤
                                              │ id          │
                                              │ nome        │
                                              │ cpf         │
                                              │ endereço    │
                                              └─────────────┘
```

### Relacionamentos

- **Pedido → Cliente**: ManyToOne (um cliente pode ter vários pedidos)
- **ItemPedido → Pedido**: ManyToOne (um pedido tem vários itens)
- **ItemPedido → Cerveja**: ManyToOne com `ON DELETE SET NULL` (preserva histórico)

## 🎯 Decisões de Design

### Por que ItemPedido armazena dados da cerveja?

**Problema:** Se deletarmos uma cerveja do estoque, pedidos antigos perderiam as informações do produto.

**Solução:** Implementamos um **snapshot dos dados** no momento da venda:
- `cervejaNome`, `cervejaSku`, `cervejaVolume` são copiados para `ItemPedido`
- A referência à `Cerveja` é opcional (nullable)
- Se a cerveja for deletada, apenas a referência fica `null`, mas os dados históricos permanecem

Isso permite:
✅ Deletar produtos descontinuados livremente  
✅ Manter relatórios e histórico íntegros  
✅ Auditar vendas passadas mesmo após mudanças no catálogo

## 🔒 Segurança

- **BCrypt** para hash de senhas
- **CSRF Protection** habilitado
- Rotas públicas apenas para login e recursos estáticos
- Sessão gerenciada pelo Spring Security

## 📱 Responsividade

O sistema é totalmente responsivo, adaptando-se a:
- 💻 Desktops (1920px+)
- 💻 Laptops (1366px)
- 📱 Tablets (768px)
- 📱 Smartphones (320px+)

Recursos mobile:
- Menu hamburguer
- Tabelas com scroll horizontal
- Botões e formulários otimizados para touch

## 🐛 Tratamento de Erros

O sistema possui validações em múltiplas camadas:

1. **Frontend**: Validação HTML5 (required, min, max)
2. **Backend**: Bean Validation (Jakarta Validation)
3. **Banco de Dados**: Constraints e foreign keys
4. **Negócio**: Validações customizadas nos Services

Mensagens de erro amigáveis são exibidas ao usuário.

## 🚧 Melhorias Futuras

- [ ] Relatórios em PDF
- [ ] Gráficos de vendas (Chart.js)
- [ ] Notificações de estoque baixo por email
- [ ] Impressão de pedidos
- [ ] Multi-tenant (suporte a várias cervejarias)
- [ ] API REST para integração com apps mobile
- [ ] Cadastro de fornecedores
- [ ] Controle de produção de cervejas

## 👨‍💻 Autor

Flávio Guilherme

- GitHub: [@FlavioGuilhermePro](https://github.com/FlavioGuilhermePro)
- LinkedIn: [Flavio Guilherme ](https://linkedin.com/in/flavioguilhermepro)
- Email: flavioguilherme.pro@gmail.com

## 🤝 Contribuindo

Contribuições são sempre bem-vindas!

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📞 Suporte

Se você tiver alguma dúvida ou problema, abra uma [Issue](https://github.com/seu-usuario/brewer/issues) no GitHub.

---

⭐ Se este projeto te ajudou, considere dar uma estrela!
