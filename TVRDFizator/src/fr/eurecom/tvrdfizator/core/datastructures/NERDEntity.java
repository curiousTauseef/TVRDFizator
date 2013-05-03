package fr.eurecom.tvrdfizator.core.datastructures;


public class NERDEntity {

	private int idEntity;
	private String label;
	private long startChar;
	private long endChar;
	private String extractorType;
	private String nerdType;
	private float confidence;
	private float relevance;
	private String extractor;
	private float startNPT;
	private float endNPT;
	private String uri;
	
	public int getIdEntity() {
		return idEntity;
	}
	public void setIdEntity(int idEntity) {
		this.idEntity = idEntity;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getStartChar() {
		return startChar;
	}
	public void setStartChar(long startChar) {
		this.startChar = startChar;
	}
	public long getEndChar() {
		return endChar;
	}
	public void setEndChar(long endChar) {
		this.endChar = endChar;
	}
	public String getExtractorType() {
		return extractorType;
	}
	public void setExtractorType(String extractorType) {
		this.extractorType = extractorType;
	}
	public String getNerdType() {
		return nerdType;
	}
	public void setNerdType(String nerdType) {
		this.nerdType = nerdType;
	}
	public float getConfidence() {
		return confidence;
	}
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	public float getRelevance() {
		return relevance;
	}
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}
	public String getExtractor() {
		return extractor;
	}
	public void setExtractor(String extractor) {
		this.extractor = extractor;
	}
	public float getStartNPT() {
		return startNPT;
	}
	public void setStartNPT(float startNPT) {
		this.startNPT = startNPT;
	}
	public float getEndNPT() {
		return endNPT;
	}
	public void setEndNPT(float endNPT) {
		this.endNPT = endNPT;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

	
	
	
}
