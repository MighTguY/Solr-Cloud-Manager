package io.github.mightguy.cloud.manager.components;

import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrCommonsException;
import java.io.IOException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Component;

@Component
public class SolrQueryComponent {

  public QueryResponse getQueryReponse(SolrRequest request, SolrClient solrClient) {
    try {
      return new QueryResponse(solrClient.request(request), solrClient);
    } catch (IOException | SolrServerException ex) {
      throw new SolrCommonsException(ex, ExceptionCode.SOLR_EXCEPTION, "Request to Solr Failed");
    }
  }
}
