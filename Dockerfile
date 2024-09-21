FROM quay.io/wildfly/wildfly:33.0.0.Final-2-jdk21 AS agendamento-email

# Indica quem é o responsável pela manutenção da imagem
LABEL MAINTAINER="Rômulo Machado Flores"

# Expõe portas que podem ser mapeadas pelo host
EXPOSE 3528 3529 8080 8443 8787 9990 9993

# Define o usuário utilizado para as operações
USER root:root

# Cria e habilita um usuário para administrar o servidor
RUN /opt/jboss/wildfly/bin/add-user.sh -u agendamento-email -p 123456 -s -e

# Copia o script de configuração do ambiente para o servidor
COPY --chown=jboss:jboss setup-config.sh /opt/jboss/setup-config.sh

# Faz a configuração do ambiente necessária para a execução da aplicação
RUN /opt/jboss/setup-config.sh