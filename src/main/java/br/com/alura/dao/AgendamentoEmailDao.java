package br.com.alura.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.alura.entidade.AgendamentoEmail;

@Stateless
public class AgendamentoEmailDao {

	@PersistenceContext
	private EntityManager entityManager;

	public AgendamentoEmailDao() {
		// construtor padrão
	}

	public List<AgendamentoEmail> listar() {
		return entityManager.createQuery("SELECT ae FROM AgendamentoEmail ae", AgendamentoEmail.class).getResultList();
	}

	public List<AgendamentoEmail> listarPorNaoAgendado() {
		return entityManager.createQuery("SELECT ae FROM AgendamentoEmail ae WHERE ae.agendado = FALSE", AgendamentoEmail.class).getResultList();
	}

	public void inserir(AgendamentoEmail agendamentoEmail) {
		entityManager.persist(agendamentoEmail);
	}

	public void alterar(AgendamentoEmail agendamentoEmail) {
		entityManager.merge(agendamentoEmail);
	}
}