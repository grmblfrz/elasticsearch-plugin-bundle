package org.xbib.elasticsearch.plugin.bundle.rest.action.isbnformat;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestStatusToXContentListener;
import org.xbib.elasticsearch.plugin.bundle.action.isbnformat.ISBNFormatAction;
import org.xbib.elasticsearch.plugin.bundle.action.isbnformat.ISBNFormatRequest;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;

/**
 * REST ISBN format action.
 */
public class RestISBNFormatterAction extends BaseRestHandler {

    @Inject
    public RestISBNFormatterAction(RestController controller) {
        super();
        controller.registerHandler(GET, "/_isbn", this);
        controller.registerHandler(GET, "/_isbn/{value}", this);
    }

    @Override
    public String getName() {
        return "ISBN";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        final String value = request.param("value");
        final ISBNFormatRequest isbnFormatRequest = new ISBNFormatRequest().setValue(value);
        return channel -> client.execute(ISBNFormatAction.INSTANCE, isbnFormatRequest,
                    new RestStatusToXContentListener<>(channel));
    }
}
