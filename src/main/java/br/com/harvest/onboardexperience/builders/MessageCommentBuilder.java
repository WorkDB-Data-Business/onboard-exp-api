package br.com.harvest.onboardexperience.builders;

import java.util.ArrayList;
import java.util.List;

import br.com.harvest.onboardexperience.domain.dtos.responses.Comment;

import br.com.harvest.onboardexperience.utils.BuilderUtils;

public class MessageCommentBuilder {
	
	private List<Comment> comments;
	
	public MessageCommentBuilder() {
		this.comments = new ArrayList<>();
	}
	
	public MessageCommentBuilder buildComment(Comment comment) {
		
		BuilderUtils.validateNullObject(comment, "Comment");
		
		this.comments.add(comment);
		
		return this;
	}
	
	public MessageCommentBuilder buildComment(String message) {
		
		BuilderUtils.validateNullObject(message, "Message");
	
		Comment comment = Comment.builder()
								 .message(message)
								 .build();
		
		this.comments.add(comment);
		
		return this;
	}
	
	public List<Comment> build(){
		return this.comments;
	}

}
