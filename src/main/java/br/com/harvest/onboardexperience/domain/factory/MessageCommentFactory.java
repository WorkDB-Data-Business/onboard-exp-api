package br.com.harvest.onboardexperience.domain.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.harvest.onboardexperience.domain.dto.Comment;
import br.com.harvest.onboardexperience.domain.exception.FactoryException;
import br.com.harvest.onboardexperience.domain.exception.enumerators.FactoryExceptionEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageCommentFactory {
	
	private List<Comment> comments;
	
	public MessageCommentFactory() {
		this.comments = new ArrayList<>();
	}
	
	public MessageCommentFactory buildComment(Comment comment) {
		
		if(Objects.isNull(comment)) {
			log.error(FactoryExceptionEnum.COMMENT_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.COMMENT_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.COMMENT_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.COMMENT_CANNOT_BE_NULL.getCause());
		}
		
		this.comments.add(comment);
		
		return this;
	}
	
	public MessageCommentFactory buildComment(String message, String messageKey) {
		
		if(Objects.isNull(message)) {
			log.error(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_CANNOT_BE_NULL.getCause());
		}
		
		if(Objects.isNull(messageKey)) {
			log.error(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getCause());
			throw new FactoryException(FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getValue(), FactoryExceptionEnum.MESSAGE_KEY_CANNOT_BE_NULL.getCause());
		}
		
		Comment comment = Comment.builder()
								 .message(message)
								 .messageKey(messageKey)
								 .build();
		
		this.comments.add(comment);
		
		return this;
	}
	
	public List<Comment> build(){
		return this.comments;
	}

}
