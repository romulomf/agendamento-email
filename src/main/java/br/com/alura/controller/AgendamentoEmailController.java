package br.com.alura.controller;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.NoArgsConstructor;

@Path("emails")
@NoArgsConstructor
public class AgendamentoEmailController {

	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;

	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listar() {
		return Response.ok(agendamentoEmailServico.listar()).build();
	}

	@POST
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response inserir(AgendamentoEmail agendamentoEmail) {
		agendamentoEmailServico.inserir(agendamentoEmail);
		return Response.status(Status.CREATED).build();
	}
}