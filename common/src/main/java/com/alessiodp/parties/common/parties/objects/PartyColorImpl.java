package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.interfaces.PartyColor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PartyColorImpl implements PartyColor {
	@Getter @Setter @NonNull private String name;
	@Getter @Setter private String command = "";
	@Getter @Setter private String code = "";
	
	@Getter @Setter private int dynamicPriority = -1;
	@Getter @Setter private int dynamicMembers = -1;
	@Getter @Setter private int dynamicKills = -1;
}
