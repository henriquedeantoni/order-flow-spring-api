# Order Flow API

## Início

Order Flow é um serviço de ordem, cadastro, envio, e agendamento de pedidos,
feito para atender micro e pequenas empresas do setor gastronômico.
Este repositório é a parte back end do projeto e foi desenvolvido em
Spring Boot, Security , Jpa ,com conexão ao banco de dados MySql.

## Arquitetura

A API segue a arquitetura Layered Architecture com separação da lógica de negócios e regras no Service,
conxão com o banco e Repositories e Controller de acessos aos endpoints.

    Controller -> Service -> Repository

## Endpoints

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
</tbody>
</table>