FROM gradle:jdk21

WORKDIR /app

COPY . .

RUN mkdir -p src/main/resources/certs \
    && openssl genrsa -out src/main/resources/certs/private.pem 2048 \
    && openssl rsa -in src/main/resources/certs/private.pem -pubout -out src/main/resources/certs/public.pem

RUN gradle installDist

CMD ./build/install/demo/bin/demo
