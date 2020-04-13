
package io.github.mightguy.cloud.manager.manager;

import io.github.mightguy.cloud.manager.exception.ExceptionCode;
import io.github.mightguy.cloud.manager.exception.SolrException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.handler.CdcrParams;
import org.springframework.stereotype.Component;


/**
 * This class {@code AliasManager} is responsible for managing all the Aliases related operations.
 * 1. Creating alias 2. Switching alias
 */
@Slf4j
@Component
public class CDCRManager {

  public Object performCDCRAction(SolrClient client, String collection,
      CdcrParams.CdcrAction action)
      throws SolrException {
    if (!(client instanceof CloudSolrClient)) {
      throw new SolrException(ExceptionCode.SOLR_EXCEPTION_CDCR,
          "Client Should be CloudSolrClient");
    }
    try {
      ModifiableSolrParams params = new ModifiableSolrParams();
      params.set(CommonParams.QT, "/cdcr");
      params.set(CommonParams.ACTION, action.toLower());
      return client.query(collection, params);
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during CDCR ", ex);
      throw new SolrException(ExceptionCode.SOLR_EXCEPTION_CDCR, ex);
    }
  }
}
