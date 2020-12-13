package jyu.ties4560.demo3_4_recipedb.exception;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import jyu.ties4560.demo3_4_recipedb.domain.ErrorMessage;

/*
 * @author Marjo Tanska
 * @version 260920
 * Catches all exceptions.
 */
//@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		exception.printStackTrace();
		
		Integer errorcode = null;
		Status status = null;
		
		switch(exception.getClass().getSimpleName()) {
		case "NotFoundException":
			errorcode = 404;
			status = Status.NOT_FOUND;
			break;
		case "ForbiddenException":
			errorcode = 403;
			status = Status.FORBIDDEN;
			break;
		case "BadRequestException":
			errorcode = 400;
			status = Status.BAD_REQUEST;
			break;
		default:
			errorcode = 500;
			status = Status.INTERNAL_SERVER_ERROR;
		}
		
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), errorcode);
		
		return Response.status(status)
				.type(MediaType.APPLICATION_JSON)
				.entity(errorMessage)
				.build();
	}
}
