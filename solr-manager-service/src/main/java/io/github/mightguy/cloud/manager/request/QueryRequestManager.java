
package io.github.mightguy.cloud.manager.request;


import io.github.mightguy.cloud.manager.config.SolrClientFactory;
import io.github.mightguy.cloud.manager.config.ZkConfiguration;
import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SearchQueryException;
import io.github.mightguy.cloud.manager.exception.SolrException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * {@code QueryRequestManager} holds the responsibility of querying on the SOLR collection.
 */
@Slf4j
@ConditionalOnBean(ZkConfiguration.class)
@Component
public class QueryRequestManager {

  @Autowired
  RequestUtil requestUtil;

  /**
   * Method to get Response from the solr. SolrParams {@link SolrParams} contains the query
   * parameters, will call getSelectQueryResponseFromClient() after fetching the {@link
   * io.github.mightguy.cloud.manager.config.SolrClientFactory}
   * using the collectionName from  getSolrClientByCollectionName
   *
   * @param params {@link SolrParams}
   * @param collectionName {@link String}
   * @return {@link QueryResponse}
   */
  public QueryResponse getResponse(SolrParams params,
      String collectionName,
      SolrRequest.METHOD method) {

    SolrClientFactory solrClientFactory = requestUtil.getSolrClientByCollectionName(collectionName);
    return getSelectQueryResponseFromClient(solrClientFactory, params, method, "");
  }


  /**
   * Method to get Response from the solr. SolrParams {@link SolrParams} contains the query
   * parameters, will call getSelectQueryResponseFromClient() after fetching the {@link
   * SolrClientFactory} using the collectionName from  getSolrClientByCollectionName
   *
   * @param params {@link SolrParams}
   * @param collectionName {@link String}
   * @return {@link QueryResponse}
   */
  public QueryResponse getResponse(SolrParams params,
      String collectionName,
      SolrRequest.METHOD method,
      String requestPath) {

    SolrClientFactory solrClientFactory = requestUtil.getSolrClientByCollectionName(collectionName);
    return getSelectQueryResponseFromClient(solrClientFactory, params, method, requestPath);
  }


  /**
   * Method to get Response from the solr. SolrParams {@link SolrParams} contains the query
   * parameters
   *
   * @param solrClientFactory {@link SolrClientFactory}
   * @param solrParams {@link SolrParams}
   * @param requestPath {@link String}
   * @return {@link QueryResponse} queryResponse from SOLR
   * @throws {@link SearchQueryException}
   */
  protected QueryResponse getSelectQueryResponseFromClient(SolrClientFactory solrClientFactory,
      SolrParams solrParams,
      SolrRequest.METHOD method,
      String requestPath) {
    try {
      QueryRequest queryRequest = new QueryRequest(solrParams);
      queryRequest.setMethod(method);
      queryRequest.setPath(requestPath);
      NamedList<Object> result = solrClientFactory.getClient().request(queryRequest);
      return new QueryResponse(result, solrClientFactory.getClient());
    } catch (SolrServerException | IOException ex) {
      String message =
          "Exception while performing query. [" + requestPath + "] QueryParams[" + solrParams + "]";
      log.error(message, ex);
      throw new SearchQueryException(ExceptionCode.SEARCH_QUERY_EXCEPTION, ex);
    }
  }

  public QueryResponse getQueryReponse(SolrRequest request, SolrClient solrClient) {
    try {
      return new QueryResponse(solrClient.request(request), solrClient);
    } catch (IOException | SolrServerException ex) {
      throw new SolrException(ex, ExceptionCode.SOLR_EXCEPTION, "UNABLE TO FETCH ALIASES");
    }
  }

}
