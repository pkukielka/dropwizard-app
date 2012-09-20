package com.pkukielka.dropwizardapp;

import com.pkukielka.dropwizardapp.bundles.SwaggerBundle;
import com.pkukielka.dropwizardapp.resources.TestResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

public class DropwizardAppService extends Service<Configuration> {
    public static void main(String[] args) throws Exception {
        new DropwizardAppService().run(args);
    }

    public DropwizardAppService() {
        super("DropwizardAppService");
        addBundle(new SwaggerBundle());
    }

    @Override
    protected void initialize(Configuration configuration, Environment environment) throws Exception {
        environment.addResource(TestResource.class);
    }
}
