FROM gradle:jdk21

WORKDIR /app

COPY . .

RUN gradle installDist

CMD ./build/install/demo/bin/demo
