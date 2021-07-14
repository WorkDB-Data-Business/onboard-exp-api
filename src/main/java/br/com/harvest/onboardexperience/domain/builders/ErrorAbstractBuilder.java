//package br.com.harvest.onboardexperience.domain.builders;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//import org.apache.commons.lang3.ObjectUtils;
//
//import br.com.harvest.onboardexperience.domain.dto.responses.Comment;
//import br.com.harvest.onboardexperience.domain.dto.responses.Message;
//import br.com.harvest.onboardexperience.domain.dto.responses.MessageError;
//import br.com.harvest.onboardexperience.domain.enumerators.MessageEnum;
//import br.com.harvest.onboardexperience.domain.enumerators.interfaces.EnumInterface;
//import br.com.harvest.onboardexperience.domain.enumerators.interfaces.ExceptionEnumInterface;
//import br.com.harvest.onboardexperience.domain.exceptions.FactoryException;
//import br.com.harvest.onboardexperience.domain.exceptions.enumerators.FactoryExceptionEnum;
//
//public abstract class ErrorAbstractBuilder {
//	
//	private MessageErrorBuilder messageErrorFactory;
//	private MessageCommentBuilder messageCommentFactory;
//	private List<EnumInterface> comments;
//	private List<ExceptionEnumInterface> errors;
//	private EnumInterface message;
//	
//	private static final List<EnumInterface> DEFAULT_COMMENT = Arrays.asList(MessageEnum.GENERIC_ERROR_MESSAGE);
//	private static final List<ExceptionEnumInterface> DEFAULT_ERROR = Arrays.asList(FactoryExceptionEnum.ERROR_FACTORY_BUILD);
//	private static final EnumInterface DEFAULT_MESSAGE = MessageEnum.GENERIC_ERROR_MESSAGE;
//	
//	public ErrorAbstractBuilder() {
//		this.messageErrorFactory = new MessageErrorBuilder();
//		this.messageCommentFactory = new MessageCommentBuilder();
//	}
//	
//	private List<MessageError> buildErrors() {
//		
//
//		if(ObjectUtils.isEmpty(this.getErrors())) {
//			throw new FactoryException(FactoryExceptionEnum.ERRORS_CANNOT_BE_NULL);
//		}
//
//		for(ExceptionEnumInterface error : this.getErrors()) {
//			this.messageErrorFactory.buildError(error.getValue(),
//					error.getCause(),
//					error.getValueKey(),
//					error.getCauseKey())
//					.addError();
//		}
//		
//		return this.messageErrorFactory.build();
//	}
//	
//	public Message build() {
//		
//		return new MessageBuilder().buildMessage(this.getMessage())
//										  .withErrors(this.buildErrors())
//										  .withComments(this.buildComments())
//										  .build();
//	}
//	
//	
//	private List<Comment> buildComments(){
//		
//		for(EnumInterface comment : this.getComments()) {
//			this.messageCommentFactory.buildComment(comment.getValue(), comment.getValueKey()).build();
//		}
//		
//		return this.messageCommentFactory.build();
//	}
//	
//	protected List<EnumInterface> getComments(){
//		return ObjectUtils.isNotEmpty(comments) ? comments : DEFAULT_COMMENT;
//	}
//	
//	protected List<ExceptionEnumInterface> getErrors(){
//		return ObjectUtils.isNotEmpty(errors) ? errors : DEFAULT_ERROR;
//	}
//	
//	protected EnumInterface getMessage(){
//		return Objects.nonNull(message) ? message : DEFAULT_MESSAGE;
//	}
//	
//	public ErrorAbstractBuilder setMessage(EnumInterface message) {
//		this.message = message;
//		return this;
//	}
//	
//	public ErrorAbstractBuilder setErrors(List<ExceptionEnumInterface> errors) {
//		this.errors = errors;
//		return this;
//	}
//	
//	public ErrorAbstractBuilder setError(ExceptionEnumInterface error) {
//		this.errors = Arrays.asList(error);
//		return this;
//	}
//	
//	public ErrorAbstractBuilder setComments(List<EnumInterface> comments) {
//		this.comments = comments;
//		return this;
//	}
//	
//	public ErrorAbstractBuilder setComment(EnumInterface comment) {
//		this.comments = Arrays.asList(comment);
//		return this;
//	}
//	
//}
