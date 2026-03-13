FROM gradle:9.3.1-jdk21

WORKDIR /app

COPY . .

RUN gradle installDist

CMD ./build/install/demo/bin/demo
