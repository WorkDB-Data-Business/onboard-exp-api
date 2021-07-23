#vars
ROOT=./
CONFIG_DIR=config
SHELL := /bin/bash

GAS_DIR=gas

APP_NAME=onboardexperience
# Functions

# checks if the users really wants to perfom the action
check:
    @echo -n "Are you sure? [y/N] " && read ans && [ $${ans:-N} = y ]

# make commands
help:
	@echo "--------------------------------------------HELP---------------------------------------------"
	@echo "install-env-variables-windows -> Instala as variaveis de ambiente no Windows. Requer que reinicie a maquina."
	@echo "remove-env-variables-windows -> Remove as variaveis de ambiente no Windows. Requer que reinicie a maquina."
	@echo "install-env-variables-linux -> Instala as variaveis de ambiente no Linux. Requer que reinicie a maquina."
	@echo "remove-env-variables-linux ->  Remove as variaveis de ambiente no Windows. Requer que reinicie a maquina."
	@echo "install -> Instala as dependencias do projeto descritas no pom.xml."
	@echo "build -> Instala as dependencias do projeto descritas no pom.xml e gera o .jar."
	@echo "run -> Cria/recria os containers do Docker conforme a descricao no docker-compose e Dockerfiles."
	@echo "reset-database -> apaga os volumes do container do banco de dados e o recria."

reset-database: check
	docker-compose down -v
	docker-compose up -d database

.ONESHELL:

install:
	@mvn clean install -Dmaven.test.skip=true 

build:
	@mvn clean package -Dmaven.test.skip=true

run: build
	@docker-compose down -v
	@docker-compose -f docker-compose.yml up --build -d --force-recreate

install-env-variables-windows: configure-exec-policy
	powershell .\Scripts\.\add_env_variables.ps1

remove-env-variables-windows: configure-exec-policy
	powershell .\Scripts\.\remove_env_variables.ps1

configure-exec-policy:
	powershell Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy Unrestricted

install-env-variables-linux:
	 ./Scripts/./add_env_variables.sh

remove-env-variables-linux:
	 ./Scripts/./remove_env_variables.sh
