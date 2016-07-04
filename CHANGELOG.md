# v0.7.4 - 14 May 2016

Upgrade to Keycloak 1.9.8.

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