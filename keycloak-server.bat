rmdir /s /q -rf keycloak-server\data
mkdir keycloak-server\data\import
copy keycloak-server-docker\keycloak-realm.json keycloak-server\data\import
set KEYCLOAK_ADMIN=admin
set KEYCLOAK_ADMIN_PASSWORD=admin
call keycloak-server\bin\kc.bat start-dev --import-realm
