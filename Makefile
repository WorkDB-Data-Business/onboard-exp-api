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
	@echo "Em construcao."
	@echo

reset-db:
	docker-compose down -v
	docker-compose up -d db1


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
