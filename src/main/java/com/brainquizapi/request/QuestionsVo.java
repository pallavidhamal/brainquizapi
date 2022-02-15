package com.brainquizapi.request;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class QuestionsVo {

	private String id;
	private String questionName;
	private Float questionNo;
    private Set<AnswersVo> answers = new HashSet<>();
}
