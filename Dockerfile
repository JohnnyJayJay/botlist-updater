FROM johnnyjayjay/leiningen:openjdk11 AS build
WORKDIR /usr/src/botlist-updater
COPY project.clj .
RUN lein deps

FROM build
COPY . .
CMD ["lein", "run"]
