package br.com.alura.servico;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.alura.dao.AgendamentoEmailDao;
import br.com.alura.entidade.AgendamentoEmail;

@Stateless
public class AgendamentoEmailServico {

	@Inject
	private AgendamentoEmailDao dao;

	public AgendamentoEmailServico() {
		// construtor padrão
	}

	public List<AgendamentoEmail> listar() {
		return dao.listar();
	}

	public void inserir(AgendamentoEmail agendamentoEmail) {
		agendamentoEmail.setAgendado(Boolean.FALSE);
		dao.inserir(agendamentoEmail);
	}
}