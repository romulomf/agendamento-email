#!/bin/bash

WILDFLY_HOME=$(printenv JBOSS_HOME)
WILDFLY_MODE=${1:-'standalone'}
WILDFLY_CLI=$WILDFLY_HOME/bin/jboss-cli.sh

# Cria e habilita um usuário para administrar o servidor
# $WILDFLY_HOME/bin/add-user.sh -u agendamento-email -p 123456 -s -e

function wait_for_server() {
	until $($WILDFLY_CLI -c ":read-attribute(name=server-state)" &> /dev/null); do
		echo "Trying to connect to server ..."
		sleep 1
	done
}

echo "Starting application server"
$WILDFLY_HOME/bin/$WILDFLY_MODE.sh --start-mode=admin-only -c standalone-full.xml > /dev/null &

echo "Waiting server to be ready to accept connections and execute commands"
wait_for_server

function mariadb_module() {
	echo "Checking MariaDB module status ..."
	# Módulo do MariaDB
	local MARIADB_MODULE_DIRECTORY=$WILDFLY_HOME/modules/org/mariadb/main

	local readonly MARIADB_DRIVER_VERSION='3.1.2';

	if [[ -f $MARIADB_MODULE_DIRECTORY/mariadb-java-client-$MARIADB_DRIVER_VERSION.jar ]]; then
		echo "MariaDB module v$MARIADB_DRIVER_VERSION already installed"
		return;
	fi

	if [[ ! -f /opt/jboss/mariadb-java-client-$MARIADB_DRIVER_VERSION.jar  ]]; then
		echo "MariaDB module not found"
		echo "Downloading v$MARIADB_DRIVER_VERSION of MariaDB module library ..."
		# Faz o download do driver do mariadb para o diretório atual
		curl https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/$MARIADB_DRIVER_VERSION/mariadb-java-client-$MARIADB_DRIVER_VERSION.jar -o mariadb-java-client-$MARIADB_DRIVER_VERSION.jar
	fi

	echo "Adding MariaDB module ..."
	# Adiciona o driver do mariadb à configuração de drivers do servidor de aplicação widlfly
	$WILDFLY_CLI -c "module add --allow-nonexistent-resources --name=org.mariadb --resources=/opt/jboss/mariadb-java-client-$MARIADB_DRIVER_VERSION.jar --dependencies=javax.api,javax.transaction.api"
	echo "MariaDB module added"

	sleep 1
	echo "MariaDB JDBC Driver configuration started"

	# Adiciona a configuração do driver jdbc do módulo
	$WILDFLY_CLI -c "/subsystem=datasources/jdbc-driver=MariaDB:add(driver-name=\"MariaDB\",driver-module-name=\"org.mariadb\",driver-class-name=\"org.mariadb.jdbc.Driver\")"
	echo "MariaDB JDBC Driver configuration done"
}

mariadb_module

function data_source () {
	echo "Checking Data Source configuration ..."
	$WILDFLY_CLI -c "data-source query --name=AgendamentoEmailDS" &> /dev/null
	# Caso o data source já exista, encerra a configuração
	if [[ $? = 0 ]]; then
		echo "Data Source AgendamentoEmailDS already exists"
		return;
	fi
	# Configura o datasource utilizado pela aplicação agendamento-email
	$WILDFLY_CLI -c "data-source add --name=AgendamentoEmailDS --jndi-name=java:jboss/datasources/AgendamentoEmailDS --driver-name=MariaDB --connection-url=jdbc:mariadb://agendamento-database:3306/agendamento-email --user-name=agendamento-email --password=agendamento-email"
	echo "AgendamentoEmailDS created"
}

data_source

function jms_queue() {
	echo "Checking JMS Queue configuration ..."
	$WILDFLY_CLI -c "jms-queue query --queue-address=EmailQueue --select=[entries]" &> /dev/null
	# Caso a fila JMS já exista, encerra a configuração
	if [[ $? = 0 ]]; then
		echo "JMS Queue named EmailQueue already exists"
		return;
	fi
	# Cria a fila JMS de e-mails utilizada pela aplicação de agendamento de e-mails
	$WILDFLY_CLI -c "jms-queue add --queue-address=EmailQueue --entries=java:/jms/queue/EmailQueue"
	echo "EmailQueue created"
}

jms_queue

# O modo de desenvolvimento do weld está depreciado

#if [[ $ENVIRONMENT = 'development' ]]; then
#	echo "Setting Weld to Development Mode"
#	$WILDFLY_CLI -c "/subsystem=weld:write-attribute(name=development-mode,value=true)" &> /dev/null
#	echo "Weld is set to operate in development mode"
#fi

echo "Shuting down wildfly ..."

if	[ "$WILDFLY_MODE" = 'standalone' ]; then
	$WILDFLY_CLI -c ":shutdown" &> /dev/null
else
	$WILDFLY_CLI -c "/host=*:shutdown" &> /dev/null
fi

sleep 1

echo "Wildfly configuration done"