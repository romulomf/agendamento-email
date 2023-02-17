package br.com.alura.mdb;

import br.com.alura.entidade.AgendamentoEmail;
import br.com.alura.servico.AgendamentoEmailServico;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@MessageDriven(activationConfig = {
	@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/EmailQueue"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class AgendamentoEmailMdb implements MessageListener {

	@Inject
	private AgendamentoEmailServico agendamentoEmailServico;

	@Override
	public void onMessage(Message message) {
		try {
			AgendamentoEmail agendamentoEmail = message.getBody(AgendamentoEmail.class);
			agendamentoEmailServico.enviar(agendamentoEmail);
		} catch (JMSException e) {
			log.error(e.getMessage(), e);
		}
	}
}