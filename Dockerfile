FROM gradle:jdk21

RUN apt-get update && apt-get install -y nodejs npm

WORKDIR /app

COPY . .

RUN npm i @hexlet/java-task-manager-frontend && npx build-frontend

RUN mkdir -p src/main/resources/certs \
    && openssl genrsa -out src/main/resources/certs/private.pem 2048 \
    && openssl rsa -in src/main/resources/certs/private.pem -pubout -out src/main/resources/certs/public.pem

RUN gradle installDist

CMD ./build/install/demo/bin/demo
