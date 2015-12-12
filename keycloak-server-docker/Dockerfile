FROM jboss/keycloak:1.7.0.Final

USER jboss

ADD keycloak-realm.json /opt/jboss/keycloak/

EXPOSE 8080

CMD ["/opt/jboss/keycloak/bin/standalone.sh", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.realmName=test", "-Dkeycloak.migration.file=/opt/jboss/keycloak/keycloak-realm.json", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-b", "0.0.0.0"]