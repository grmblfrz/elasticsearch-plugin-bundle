package org.xbib.elasticsearch.plugin.bundle.common.reference;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.xbib.elasticsearch.plugin.bundle.index.mapper.reference.ReferenceMapperTypeParser;

/**
 * Reference service.
 */
public class ReferenceService extends AbstractLifecycleComponent {

    private final Injector injector;

    @Inject
    public ReferenceService(Injector injector) {
        super();
        this.injector = injector;
    }

    @Override
    protected void doStart() {
        // get the client from the injector
        Client client = injector.getInstance(Client.class);
        // copy the client to the mapper type parser
        ReferenceMapperTypeParser referenceMapperTypeParser = injector.getInstance(ReferenceMapperTypeParser.class);
        referenceMapperTypeParser.setClient(client);
    }

    @Override
    protected void doStop() {
        // nothing to stop
    }

    @Override
    protected void doClose() {
        // nothing to close
    }
}
