package mx.com.gruponordan.model;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String id;
	private String username;
	private long dateIssued;
	private String area;
	private String codeArea;


	public JwtResponse(String accessToken, String id, String username,long dateIssued, String area, String codeArea) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.dateIssued = dateIssued;
		this.area = area;
		this.codeArea = codeArea;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getDateIssued() {
		return dateIssued;
	}

	public String getArea() {
		return area;
	}
	
	public String getCodeArea() {
		return codeArea;
	}

}
