package bll;

import java.util.LinkedList;

import dto.LemmatizationDTO;

public interface ILemmatizationBO {

	void processLemmatizationForPage(int pageId, String pageContent);

	LemmatizationDTO analyzeWord(String word, int pageId);

	LinkedList<LemmatizationDTO> getLemmatizationForPage(int pageId);

}
