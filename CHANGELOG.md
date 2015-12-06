# v0.5.0 - ??

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