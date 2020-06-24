
package io.github.mightguy.cloud.solr.commons.request;


import io.github.mightguy.cloud.solr.commons.config.SolrClientFactory;
import io.github.mightguy.cloud.solr.commons.config.ZkConfiguration;
import io.github.mightguy.cloud.solr.commons.exception.ExceptionCode;
import io.github.mightguy.cloud.solr.commons.exception.SolrException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.StringUtils;
import org.apache.solr.common.params.UpdateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * {@code UpdateRequestManager} holds the responsibility of updation process on the SOLR Update
 * process involves the update request and the commit.
 */
@Slf4j
@ConditionalOnBean(ZkConfiguration.class)
@Component
public class UpdateRequestManager {

  @Autowired
  RequestUtil requestUtil;

  /**
   * This method is responsible of sending all the {@link SolrInputDocument}  to SOLR. All {@link
   * SolrInputDocument} will be wrapped under the UpdateRequest and sent to SOLR.
   *
   * This method also provide the functionality to create the batches from this list.
   *
   * @param solrClientFactory {@link SolrClientFactory}
   * @param docs {@link SolrInputDocument}
   * @param chain {@link String}
   * @param coreName {@link String}
   * @return {@link Boolean}
   */
  public boolean performUpdateRequest(SolrClientFactory solrClientFactory,
      List<SolrInputDocument> docs,
      String chain, int batchSize, String coreName) {
    coreName = coreName.concat(requestUtil.solrConfigruationProperties.getSuffix().getActive());
    if (batchSize < 1) {
      throw new SolrException(ExceptionCode.BATCH_SIZE_ERROR);
    }
    List<SolrInputDocument> myNodeList = new ArrayList<>(docs);
    long calculateCount = docs.size() / batchSize;
    log.info("Total documents which gets updated are " + docs.size() + " And batch Size is "
        + batchSize);
    long size = (docs.size() % batchSize == 0) ? calculateCount : calculateCount + 1;
    for (int i = 0; i < size; i++) {
      log.info(" creating update request  for batch " + i);
      try {
        UpdateRequest updateRequest = new UpdateRequest();
        if (!StringUtils.isEmpty(chain)) {
          updateRequest.setParam("update.chain", chain);
        }
        if (i == size - 1) {
          updateRequest.add(myNodeList.subList(i * batchSize, myNodeList.size()));
        } else {
          updateRequest.add(myNodeList.subList(i * batchSize, (batchSize * (i + 1)) - 1));
        }

        processUpdate(updateRequest, solrClientFactory, coreName);

      } catch (IOException | SolrServerException ex) {
        log.error("Exception during indexing core=" + solrClientFactory.getClient(), ex);
        throw new SolrException(ExceptionCode.SOLR_EXCEPTION_INDEXING);
      }
    }
    return true;
  }

  /**
   * Method for processing the UpdateRequest {@link UpdateRequest}
   *
   * @param updateRequest {@link UpdateRequest}
   * @param solrClientFactory {@link SolrClientFactory}
   * @param coreName {@link String}
   */
  protected void processUpdate(UpdateRequest updateRequest, SolrClientFactory solrClientFactory,
      String coreName) throws IOException, SolrServerException {
    updateRequest.process(solrClientFactory.getClient(), coreName);
  }


  /**
   * Method for sending the commit request to SOLR
   */
  public void commit(boolean optimize, String coreName) {
    try {
      SolrClientFactory solrClientFactory = requestUtil.getSolrClientByCollectionName(coreName);
      coreName = coreName.concat(requestUtil.solrConfigruationProperties.getSuffix().getActive());
      log.info("Start commit core=" + coreName);
      AbstractUpdateRequest commitCommand =
          new UpdateRequest().setAction(UpdateRequest.ACTION.COMMIT, true, true, true);
      commitCommand.getParams().set(UpdateParams.OPTIMIZE, optimize);
      commitCommand.getParams().set(UpdateParams.MAX_OPTIMIZE_SEGMENTS, 1);
      solrClientFactory.getClient().request(commitCommand, coreName);
      log.info("Commit core=" + coreName + " successful");
    } catch (SolrServerException | IOException ex) {
      log.error("Exception during commit core=" + coreName, ex);
      throw new SolrException(ExceptionCode.SOLR_EXCEPTION_COMMITING_CORE);
    }
  }


}
