FROM jboss/keycloak:4.8.3.Final
MAINTAINER alexander.schwartz@gmx.net

USER jboss

COPY keycloak-realm.json /opt/jboss/keycloak/

EXPOSE 8080

CMD ["-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.realmName=test", "-Dkeycloak.migration.file=/opt/jboss/keycloak/keycloak-realm.json", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-b", "0.0.0.0"]
