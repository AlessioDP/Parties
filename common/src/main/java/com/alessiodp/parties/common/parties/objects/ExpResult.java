package com.alessiodp.parties.common.parties.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class ExpResult {
	@Getter @Setter private int level;
	@Getter @Setter private double levelExperience;
	@Getter @Setter private double levelUpCurrent;
	@Getter @Setter private double levelUpNecessary;
	
	public ExpResult() {
		level = 1;
		levelExperience = 0;
		levelUpCurrent = 0;
		levelUpNecessary = 0;
	}
}
