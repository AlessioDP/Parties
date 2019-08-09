package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.interfaces.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class ColorImpl implements Color {
	@Getter @Setter @NonNull private String name;
	@Getter @Setter private String command = "";
	@Getter @Setter private String code = "";
	
	@Getter @Setter private int dynamicPriority = -1;
	@Getter @Setter private int dynamicMembers = -1;
	@Getter @Setter private int dynamicKills = -1;
	
	@Override
	public int hashCode() {
		return Objects.hash(name, command, code, dynamicPriority, dynamicMembers, dynamicKills);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this || other instanceof ColorImpl) {
			return Objects.equals(name, ((ColorImpl) other).name)
					&& Objects.equals(command, ((ColorImpl) other).command)
					&& Objects.equals(code, ((ColorImpl) other).code)
					&& dynamicPriority == ((ColorImpl) other).dynamicPriority
					&& dynamicMembers == ((ColorImpl) other).dynamicMembers
					&& dynamicKills == ((ColorImpl) other).dynamicKills;
		}
		return false;
	}
}
