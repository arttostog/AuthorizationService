package me.arttostog.AuthorizationService.User;

public class Response {
	private final int code;
	private final Object object;

	public Response(int code, Object object) {
		this.code = code;
		this.object = object;
	}
}
