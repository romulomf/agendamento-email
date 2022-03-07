package br.com.alura.servico;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.alura.dao.AgendamentoEmailDao;
import br.com.alura.entidade.AgendamentoEmail;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless
@NoArgsConstructor
public class AgendamentoEmailServico {

	@Inject
	private AgendamentoEmailDao dao;

	public List<AgendamentoEmail> listar() {
		return dao.listar();
	}

	public List<AgendamentoEmail> listarPorNaoAgendado() {
		return dao.listarPorNaoAgendado();
	}

	public void inserir(AgendamentoEmail agendamentoEmail) {
		agendamentoEmail.setAgendado(Boolean.FALSE);
		dao.inserir(agendamentoEmail);
	}

	public void alterar(AgendamentoEmail agendamentoEmail) {
		agendamentoEmail.setAgendado(Boolean.TRUE);
		dao.alterar(agendamentoEmail);
	}

	public void enviar(AgendamentoEmail agendamentoEmail) {
		try {			
			Thread.sleep(5000);
			log.info(String.format("O e-mail do(a) usuário(a) %s foi enviado!", agendamentoEmail.getEmail()));
		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}
}