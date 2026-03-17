# Order Flow API

## Overview

Order Flow é um serviço de ordem, cadastro, envio, e agendamento de pedidos,
feito para atender micro e pequenas empresas do setor gastronômico.
Este repositório é a parte back end do projeto e foi desenvolvido em
Spring Boot, Security , Jpa ,com conexão ao banco de dados MySql.

Há a divisão de acesso entre perfis de clientes e perfis de administradores.
A divisão é realizada com uma politica de Roles e Authorization e separadas nos endpoints em "/admin" e "/public".
Na opção adminstrador tambem é possível realizar relatórios e colher gráficos sobre o negócio como vendas por mês,
items cadastrados, vendas por região, usuário com maior compra entre outros.

## Clonando o Projeto

Para clonar o projeto, fazer o ```git clone``` do repositório.

Abrir o projeto na sua IDE de preferência, caso esteja trabalhando com o IntelliJ dar sync no ```pom.xml```
. <br>
Em seguida fazer o build.
Consultar os endpoints na seção Endpoints.

## Arquitetura

A API segue a arquitetura Layered Architecture com separação da lógica de negócios e regras no Service,
conexão com o banco e Repositories e Controller de acessos aos endpoints.

    Controller -> Service -> Repository

Tambem há uma Engine que é responsável pela contrução dos Charts e gráficos dos Dashboards,
 ela utiliza uma API Open Source JFreeChart cuja a documentação pode ser consultada aqui:
```https://www.jfree.org/jfreechart/``` 
e
```https://github.com/jfree/jfreechart```
.

Os gráficos são enviados nas respostas das requisições sob a extensão ```.svg``` .
Os gráficos gerados são do tipo Pizza, Barras, Times Series e Dispersão.

### Segurança com SPRING SECURITY

A camada de segurança é providenciada pelo Spring Security e JWT Authentication.

A classe ```AuthTokenFilter```  intercepta as requests e filtra conforme o endereço se ele é publico ou administrador.
Ela faz a leitura no conjunto de ROLES que determinado usuário possui, ```ROLE_ADMIN``` , ```ROLE_USER```, ```ROLE_CLIENT```
, ```ROLE_ATTENDANCE```.

A classe ```JwtUtils```  fica encarregada de gerar e validar o Token de acesso JWT.

A classe ```AuthEntryPointJwt```  é responsável pelo tratamento de erros de autenticação quando há a tentativa de um
usuário acessar um endpoint sem ser autenticado ou um token inválido. Ela utiliza um registro de log sempre que há 
a tentativa de acesso sem autorização. O status de retorno é ```HTTP 401```.

Na classe ```UserDetailsServiceImpl``` implementa a interface UserDetailService e carrega as informações do usuário.

Na classe ```WebSecurityConfig``` têm-se a configuração da segurança,
há a função de criptografia e definida pelo encoder BCryptPasswordEncoder que criptografa a senha de acesso.
Tambem é configurado os filtros de acesso e encadeamento no método ```springSecFilterChain``` o que é permitido para qualquer usuário, público ou admin.


## Endpoints

Para começar, deve-se autenticar o usuário caso já tenha credencial criada senão criar um usuário e em seguida fazer o signin.
O JWT fica salvo nos cookies.

<p style="color: #66a; line-height: 1.6; margin-bottom: 15px;">A lista completa dos endpoints tambem pode ser consultada via Swagger UI em `http://localhost:8080/api`. Abaixo alguns exemplos de integrações:</p>

<table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
<thead>
<tr style="background-color: #e0e0e0; color: #34495e;">
<th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Fluxo/Tela do Frontend</th>
<th style="padding: 10px; border: 1px solid #ddd; text-align: left;">Endpoint do Backend (Método HTTP, Caminho)</th>
<th style="padding: 10px; border: 1px solid #ddd; text-align: left;">DTOs (Requisição/Resposta)</th>
</tr>
</thead>
<tbody>
<tr style="background-color: #f5f5f5;">
<td colspan="3" style="padding: 10px; border: 1px solid #555; font-weight: bold; color: #34495e;">Fluxo de Autenticação</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Registro de Clientes</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`POST /v1/auth/signup`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`SignupRequest` / `Auth message`</td>
</tr>
<tr style="background-color: #f8f8f8;">
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Autenticação de usuário</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`POST /v1/auth/signin`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`LoginRequest` / `AuthenticationResult`</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Get Username</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`GET /v1/auth/username`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`Authentication` / `string username`</td>
</tr>
<tr style="background-color: #f8f8f8;">
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Get User Details</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`POST /v1/auth/user`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`Authentication` / `userDetailsInfo`</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Realizar Sign Out</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`GET /v1/auth/signout`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`Authentication` / `Message Response`</td>
</tr>
<tr style="background-color: #f2f2f2;">
<td colspan="3" style="padding: 10px; border: 1px solid #ddd; font-weight: bold; color: #34495e;">Gerenciamento de Items</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Listar todos os itens</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`GET /v1/public/items`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">``/`ItemResponse`</td>
</tr>
<tr style="background-color: #f8f8f8;">
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Criar item</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`POST /v1/admin/items/{categoryId}/item`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`itemDTO` / `savedItemDTO`</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Atualizar item e categoria</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`PUT /admin/items/{itemId}/item/{categoryId}`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">``ItemDTO/`ItemDTO`</td>
</tr>
<tr style="background-color: #f8f8f8;">
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Atualizar status de items</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`PUT /v1/admin/items/{itemId}/item/{status}`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`ItemDTO` / `ItemDTO`</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Atualizar imagem do item</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`PUT /admin/items/{itemId}/image/{imageId}`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">``ItemDTO/`ItemDTO`</td>
</tr>
<tr style="background-color: #f8f8f8;">
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Consultar item pelo seu ID</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`GET /v1/admin/items/{categoryId}`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`` / `ItemResponse`</td>
</tr>
<tr>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">Consultar item por trecho da descrição</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">`GET /admin/items/{categoryId}/{keyword}`</td>
<td style="padding: 10px; border: 1px solid #ddd; color: #555;">``/`ItemResponse`</td>
</tr>
</tbody>
</table>