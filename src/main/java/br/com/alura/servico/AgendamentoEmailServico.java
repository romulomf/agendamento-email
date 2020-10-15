package br.com.alura.servico;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.alura.dao.AgendamentoEmailDao;
import br.com.alura.entidade.AgendamentoEmail;

@Stateless
public class AgendamentoEmailServico {

	private static final Logger LOOGER = Logger.getLogger(AgendamentoEmail.class.getName());

	@Inject
	private AgendamentoEmailDao dao;

	public AgendamentoEmailServico() {
		// construtor padrão
	}

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
			LOOGER.info(String.format("O e-mail do(a) usuário(a) %s foi enviado!", agendamentoEmail.getEmail()));
		} catch (InterruptedException e) {
			LOOGER.warning(e.getMessage());
		}
	}
}