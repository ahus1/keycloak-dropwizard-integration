# v1.1.4-SNAPSHOT - 26 August 2020

Upgrading to latest Keycloak 11.0.1 thanks to @gjvoosten #39
No API changes for your code.

# v1.1.3 - 23 July 2020

Upgrading to latest Keycloak 11.0.0 and Dropwizard 2.0.12 thanks to @gjvoosten #35
No API changes for your code.

# v1.1.2 - 16 June 2020

Upgrading to latest Keycloak 10.0.2 thanks to @gjvoosten #31
Upgrade to httpclient dependency.
No API changes for your code.

# v1.1.1 - 25 March 2019

Supporting policy enforcer/authz support by makeing JaxrsHttpFacade implement OIDCHttpFacade #25

# v1.1.0 - 24 February 2019

Update to Keycloak 4.8.9 and Dropwizard 1.3.9. No API changes for your code. Minor change to allow for latest Keycloak library.

# v1.0.0 - 22 March 2017

Update to Keycloak 3.0.0 and Dropwizard 1.1.0 thanks to @maksymgendin and @reneploetz

# v0.9.1 - 07 March 2017

Upgrade to Keycloak 2.5.4, fixes by @reneploetz. Previous Keycloak 2.x versions should still work. Update to Dropwizard 1.0.6.

# v0.9.0 - 10 August 2016

Upgrade to Keycloak 2.1.0 and Dropwizard 1.0. Some code changes due to the Dropwizard upgrade.

User who want to stay on Dropwizard 0.9 should use the 0.8.x releases.

# v0.8.0 - 07 July 2016

Upgrade to Keycloak 2.0.0. No code or API changes.

# v0.7.4 - 26 June 2016

Upgrade to Keycloak 1.9.8. No code or API changes.

# v0.7.3 - 14 May 2016

Upgrade to Keycloak 1.9.4.
Don't start authentication twice #6 as reported by @anthonyraymond.

# v0.7.2 - 01 May 2016

Upgrade to Keycloak 1.9.3. No code or API changes.

# v0.7.1 - 26 Apr 2016

Support `use-resource-role-mappings` for dropwizard adapter thanks to @anthonyraymond. #5

# v0.7.0 - 28 Feb 2016

Upgrading to Keycloak 1.9.
  * You'll now need to include dropwizard-jackson module if you haven't done that before, as Keycloak client will assume that it is provided.
  * Keycloak annotated their `AdapterConfig` with JsonProperty elements, therefore you can now (and must now) use the hyphenated configuration element names instead of the camelcase ones in you `config.yml`. Example: `authServerUrl` is now `auth-server-url`, and `realmKey` is `realm-public-key`.

# v0.6.0 - 29 Jan 2016

Upgrading to Keycloak 1.8. No API changes.

# v0.5.0 - 12 Nov 2015

Upgrading to Keycloak 1.7

# v0.4.1 - 24 Nov 2015

Fix 500er response when bearer only and request without Authentication header. #4

# v0.4.0 - 19 Nov 2015

Update to Keycloak 1.6.
Although this is quite a major change for Keycloak, the API for our consumers didn't change.
Therefore it is a patch only.

This release has also better support to combine a frontend public client with a backend bearer token only server.

# v0.3.0 - 04 Nov 2015

Works with Keycloak 1.5 and Dropwizard 0.9.

When upgrading, you'll notice that `@Auth(required=false)` is no longer supported by Dropwizard 0.9.
Instead instead inject the Security context like shown below.

    @GET
    @Path("/logout")
    public LogoutView logout(@Context SecurityContext context) throws ServletException {
        if (context.getUserPrincipal() != null) {
            request.logout();
        }
    }

This is also the first release that supports the `@RolesAllowed` annotation.
This was possible due to the Dropwizard 0.9 changes.

    @GET
    @RolesAllowed("user")
    public DrawView show(@Auth User auth) {
        DrawBean bean = new DrawBean();
        DrawView view = new DrawView(bean);
        bean.setName(auth.getName());
        return view;
    }

# v0.2.1 - 15 Oct 2015

1st release to Maven Central.
Works with Keycloak 1.4/1.5 and Dropwizard 0.8.

# v0.2.0 - 09 Oct 2015

Initial release. Wrapped up as a Dropwizard Bundle. Handles full OAuth flow including form submissions.
Works with Keycloak 1.4/1.5 and Dropwizard 0.8.
