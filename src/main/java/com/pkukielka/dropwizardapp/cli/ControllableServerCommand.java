package com.pkukielka.dropwizardapp.cli;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.dropwizard.AbstractService;
import com.yammer.dropwizard.cli.ServerCommand;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.ServerFactory;
import com.yammer.dropwizard.logging.Log;
import org.apache.commons.cli.CommandLine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ShutdownHandler;

import java.io.IOException;

public class ControllableServerCommand<T extends Configuration> extends ServerCommand<T> {
    public ControllableServerCommand(Class<T> configurationClass) {
        super(configurationClass);
    }

    private void addShutdownHandler(Server server) {
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(server.getHandlers());
        handlers.addHandler(new ShutdownHandler(server, "stopJetty"));
        server.setHandler(handlers);
    }

    @Override
    protected void run(AbstractService<T> service, T configuration, CommandLine params) throws Exception {
        final Environment environment = new Environment(service, configuration);
        service.initializeWithBundles(configuration, environment);
        final Server server = new ServerFactory(configuration.getHttpConfiguration(),
                service.getName()).buildServer(environment);
        final Log log = Log.forClass(ControllableServerCommand.class);

        addShutdownHandler(server);

        logBanner(service, log);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error(e, "Unable to start server, shutting down");
            server.stop();
        }
    }

    private void logBanner(AbstractService<T> service, Log log) {
        try {
            final String banner = Resources.toString(Resources.getResource("banner.txt"), Charsets.UTF_8);
            log.info("Starting {}\n{}", service.getName(), banner);
        } catch (IllegalArgumentException ignored) {
            log.info("Starting {}", service.getName());
        } catch (IOException ignored) {
            log.info("Starting {}", service.getName());
        }
    }
}
