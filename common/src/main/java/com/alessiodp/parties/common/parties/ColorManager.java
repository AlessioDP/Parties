package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.ColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.api.interfaces.Color;
import lombok.Getter;

import java.util.Set;

public class ColorManager {
	
	@Getter private Set<ColorImpl> colorList;
	
	public ColorManager() {
		reload();
	}

	public void reload() {
		colorList = ConfigParties.COLOR_LIST;
	}
	
	public Color searchColorByName(String name) {
		Color ret = null;
		if (name != null && !name.isEmpty()) {
			for (Color pc : colorList) {
				if (pc.getName().equalsIgnoreCase(name)) {
					ret = pc;
					break;
				}
			}
		}
		return ret;
	}
	
	public Color searchColorByCommand(String cmd) {
		Color ret = null;
		for (Color pc : colorList) {
			if (pc.getCommand().equalsIgnoreCase(cmd)) {
				ret = pc;
				break;
			}
		}
		return ret;
	}
	
	public void loadDynamicColor(PartyImpl party) {
		if (ConfigParties.COLOR_ENABLE && ConfigParties.COLOR_DYNAMIC && party.getColor() == null) {
			Color selected = null;
			for (Color pc : colorList) {
				boolean found = false;
				if (pc.getDynamicMembers() > -1) {
					if (party.getMembers().size() >= pc.getDynamicMembers())
						found = true;
				} else if (pc.getDynamicKills() > -1) {
					if (party.getKills() >= pc.getDynamicKills())
						found = true;
				}
				
				if (found) {
					if (selected == null || pc.getDynamicPriority() > selected.getDynamicPriority())
						selected = pc;
				}
			}
			
			if (selected != null)
				party.setDynamicColor(selected);
		}
	}
}
