package com.alessiodp.parties.common.parties.objects;

import lombok.Getter;
import lombok.Setter;

public class ExpResult {
	@Getter @Setter private int level;
	@Getter @Setter private int currentExperience;
	@Getter @Setter private int necessaryExperience;
	
	public ExpResult() {
		level = 1;
		currentExperience = 0;
		necessaryExperience = 0;
	}
}
