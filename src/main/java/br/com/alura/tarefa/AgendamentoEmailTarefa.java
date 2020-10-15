package br.com.alura.tarefa;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;

@Singleton
public class AgendamentoEmailTarefa {

	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;

	public AgendamentoEmailTarefa() {
		// construtor padrão
	}

	@Schedule(hour = "*", minute = "*", second = "*/10")
	public void enviar() {
		List<AgendamentoEmail> emailsNaoAgendados = agendamentoEmailServico.listarPorNaoAgendado();
		emailsNaoAgendados.forEach(email -> {
			context.createProducer().send(queue, email);
			agendamentoEmailServico.alterar(email);
		});
	}
}