.PHONY: up down build run test clean

up:
	docker compose up 

db: 
	docker compose up db

down:
	docker compose down

test:
	mvn test

clean:
	mvn clean
	rm -rf target/
