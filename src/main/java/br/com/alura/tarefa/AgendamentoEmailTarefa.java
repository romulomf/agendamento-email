package br.com.alura.tarefa;

import java.util.List;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;
import jakarta.annotation.Resource;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import lombok.NoArgsConstructor;

@Singleton
@NoArgsConstructor
public class AgendamentoEmailTarefa {

	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;

	@Schedule(hour = "*", minute = "*", second = "*/10")
	public void enviar() {
		List<AgendamentoEmail> emailsNaoAgendados = agendamentoEmailServico.listarPorNaoAgendado();
		emailsNaoAgendados.forEach(email -> {
			context.createProducer().send(queue, email);
			agendamentoEmailServico.alterar(email);
		});
	}
}