package com.alessiodp.parties.common.parties.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class ExpResult {
	@Getter @Setter private int level;
	@Getter @Setter private int levelExperience;
	@Getter @Setter private int currentExperience;
	@Getter @Setter private int necessaryExperience;
	
	public ExpResult() {
		level = 1;
		levelExperience = 0;
		currentExperience = 0;
		necessaryExperience = 0;
	}
}
