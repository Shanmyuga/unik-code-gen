package ${package}.response;

public class ${className}Response {
	private String statusCode;
	private String status;
	private String statusMessage;
	
	public ${className}Response(){}

	public ${className}Response(String statusCode, String status, String statusMessage) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.statusMessage = statusMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	
	
}
