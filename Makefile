#vars
ROOT=./
CONFIG_DIR=config


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

package:
	@mvn clean package -Dmaven.test.skip=true

.ONESHELL:

install:
	@mvn clean package -Dmaven.test.skip=true 

run: 
	@docker-compose down -v
	@docker-compose -f docker-compose.yml up --build -d --force-recreate 

