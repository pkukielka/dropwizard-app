package com.pkukielka.dropwizardapp.resources;

import com.sun.jersey.api.core.ResourceConfig;
import com.wordnik.swagger.jaxrs.ApiListingResourceJSON;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.lang.reflect.Method;
import java.util.Set;

@Path("/resources.json")
@Produces(MediaType.APPLICATION_JSON)
public class ApiListingResource extends ApiListingResourceJSON {

    private class ResourceClassesMethodInterceptor implements MethodInterceptor {
        final ResourceConfig resourceConfig;

        public ResourceClassesMethodInterceptor(ResourceConfig resourceConfig) {
            this.resourceConfig = resourceConfig;
        }

        private Set<Class<?>> mergeResources(Set<Class<?>> resourceClasses, Set<Object> resourceSingletons) {
            for (Object singleton : resourceSingletons) {
                resourceClasses.add(singleton.getClass());
            }
            return resourceClasses;
        }

        public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            return "getRootResourceClasses".equals(method.getName())
                    ? mergeResources(resourceConfig.getRootResourceClasses(), resourceConfig.getRootResourceSingletons())
                    : methodProxy.invokeSuper(resourceConfig, args);
        }
    }

    @GET
    @Override
    public Response getAllApis(@Context ServletConfig sc, @Context ResourceConfig rc,
                               @Context HttpHeaders headers, @Context UriInfo uriInfo) {
        Object resourceConfigDelegate = Enhancer.create(ResourceConfig.class, new ResourceClassesMethodInterceptor(rc));
        return super.getAllApis(sc, (ResourceConfig) resourceConfigDelegate, headers, uriInfo);
    }
}