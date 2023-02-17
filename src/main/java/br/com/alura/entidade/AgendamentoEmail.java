package br.com.alura.entidade;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "agendamentoemail")
@Data
public class AgendamentoEmail implements Serializable {

	private static final long serialVersionUID = 2306066603540510413L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "assunto", nullable = false)
	private String assunto;

	@Column(name = "mensagem", nullable = false)
	private String mensagem;

	@Column(name = "agendado", nullable = false)
	private Boolean agendado;
}