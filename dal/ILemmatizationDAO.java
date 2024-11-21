package dal;

import java.util.List;

import dto.LemmatizationDTO;

public interface ILemmatizationDAO {

	void addLemmatization(LemmatizationDTO lemmatization);

	List<LemmatizationDTO> getLemmatizationForPage(int pageId);

	boolean isLemmatizationSavedForPage(int pageId, String newContent);

}
