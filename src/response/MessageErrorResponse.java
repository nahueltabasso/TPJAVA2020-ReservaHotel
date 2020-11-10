package response;

public class MessageErrorResponse {

	private String message;

	public MessageErrorResponse() {
	}

	public MessageErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
