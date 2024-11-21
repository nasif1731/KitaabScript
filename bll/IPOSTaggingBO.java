package bll;

import java.util.LinkedList;

import dto.POSTaggingDTO;

public interface IPOSTaggingBO {

	void processPOSTaggingForPage(int pageId, String pageContent);

	LinkedList<POSTaggingDTO> getPOSTaggingForPage(int pageId);

	POSTaggingDTO analyzedWord(String word, int pageId);

}