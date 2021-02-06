package entities;

public class PasswordResetToken {

	private Long id;
	private Long idUsuario;
	private String token;

	public PasswordResetToken() {}
	
	public PasswordResetToken(Long id, Long idUsuario, String token) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
