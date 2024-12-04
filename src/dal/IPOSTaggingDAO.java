package dal;

import java.util.List;

import dto.POSTaggingDTO;

public interface IPOSTaggingDAO {

	void addPOSTagging(POSTaggingDTO posTagging);

	List<POSTaggingDTO> getPOSTaggingForPage(int pageId);

	boolean isPOSTaggingSavedForPage(int pageId, String newContent);

}
