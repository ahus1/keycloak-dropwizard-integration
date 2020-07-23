FROM jboss/keycloak:11.0.0
MAINTAINER alexander.schwartz@gmx.net

USER jboss

COPY keycloak-realm.json /opt/jboss/keycloak/

EXPOSE 8080

CMD ["-Dkeycloak.migration.action=import", "-Dkeycloak.profile.feature.scripts=enabled", "-Dkeycloak.profile.feature.upload_scripts=enabled", "-Dkeycloak.migration.provider=singleFile", "-Dkeycloak.migration.realmName=test", "-Dkeycloak.migration.file=/opt/jboss/keycloak/keycloak-realm.json", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-b", "0.0.0.0"]
