package br.com.alura.dao;

import java.util.List;

import br.com.alura.entidade.AgendamentoEmail;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class AgendamentoEmailDao {

	@PersistenceContext
	private EntityManager entityManager;

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