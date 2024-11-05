### CREACION Y PREPARACION DE IMAGEN
ARG JDK_IMAGE=registry.global.ccc.srvb.cl.paas.cloudcenter.corp/dss-dev-chi/openjdk:17-jdk-slim

FROM ${JDK_IMAGE} as builder

ENV USER=dss-user
ENV UID=10001
#ENV GROUP=dss-group
ENV GROUP=root

#RUN addgroup --system "${GROUP}" && adduser --shell /bin/sh --disabled-password --gecos "" "${USER}" --uid "${UID}" --ingroup "${GROUP}"
RUN adduser --shell /bin/sh --disabled-password --gecos "" "${USER}" --uid "${UID}" --ingroup "${GROUP}"

USER "${USER}":"${GROUP}"

WORKDIR application

ARG JAR_FILE

COPY target/${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

### CREACION DE IMAGEN
FROM ${JDK_IMAGE}

USER "${USER}":"${GROUP}"

WORKDIR application

COPY --from=builder /application/dependencies ./
COPY --from=builder /application/spring-boot-loader ./
COPY --from=builder /application/snapshot-dependencies ./
COPY --from=builder /application/application ./

RUN chgrp -R 0 /application && chmod -R g=u /application

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
