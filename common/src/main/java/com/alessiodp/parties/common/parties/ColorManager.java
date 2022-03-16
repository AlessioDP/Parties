package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.api.interfaces.PartyColor;
import com.alessiodp.parties.common.utils.PartiesPermission;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class ColorManager {
	
	@Getter private Set<PartyColorImpl> colorList;
	
	public ColorManager() {
		reload();
	}

	public void reload() {
		colorList = ConfigParties.ADDITIONAL_COLOR_LIST;
	}
	
	public PartyColor searchColorByName(String name) {
		PartyColor ret = null;
		if (name != null && !name.isEmpty()) {
			for (PartyColor pc : colorList) {
				if (pc.getName().equalsIgnoreCase(name)) {
					ret = pc;
					break;
				}
			}
		}
		return ret;
	}
	
	public void loadDynamicColor(PartyImpl party) {
		if (ConfigParties.ADDITIONAL_COLOR_ENABLE && ConfigParties.ADDITIONAL_COLOR_DYNAMIC && party.getColor() == null) {
			PartyColor selected = null;
			for (PartyColor pc : colorList) {
				boolean found = false;
				if (pc.getDynamicMembers() > -1) {
					if (party.getMembers().size() >= pc.getDynamicMembers())
						found = true;
				} else if (pc.getDynamicKills() > -1 && party.getKills() >= pc.getDynamicKills()) {
					found = true;
				}
				
				if (found && (selected == null || pc.getDynamicPriority() > selected.getDynamicPriority())) {
					selected = pc;
				}
			}
			
			if (selected != null)
				party.setDynamicColor(selected);
		}
	}
	
	public Set<PartyColorImpl> getAvailableColors(User user) {
		if (user.hasPermission(PartiesPermission.USER_COLOR))
			return colorList;
		
		Set<PartyColorImpl> ret = new HashSet<>();
		for (PartyColorImpl color : colorList) {
			if (user.hasPermission(PartiesPermission.USER_COLOR + "." + color.getName()))
				ret.add(color);
		}
		return ret;
	}
}
