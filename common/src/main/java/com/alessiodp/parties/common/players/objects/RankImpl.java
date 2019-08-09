package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.api.interfaces.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@AllArgsConstructor
public class RankImpl implements Rank {
	@Getter @Setter @NonNull private String configName;
	@Getter @Setter private String name = "";
	@Getter @Setter private String chat = "";
	@Getter @Setter private int level = 1;
	@Getter @Setter private List<String> permissions;
	private boolean def = false;
	
	@Override
	public void setDefault(boolean def) {
		this.def = def;
	}
	
	@Override
	public boolean isDefault() {
		return def;
	}
	
	public boolean havePermission(String p) {
		boolean ret = false;
		for (String perm : permissions) {
			if ("*".equals(perm)) {
				ret = true;
				break;
			}
			
			if (perm.equalsIgnoreCase("-" + p)) {
				break;
			} else if (perm.equalsIgnoreCase(p)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(configName, name, chat, level, def, permissions);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this || other instanceof RankImpl) {
			return Objects.equals(configName, ((RankImpl) other).configName)
					&& Objects.equals(name, ((RankImpl) other).name)
					&& Objects.equals(chat, ((RankImpl) other).chat)
					&& level == ((RankImpl) other).level
					&& def == ((RankImpl) other).def
					&& Objects.equals(permissions, ((RankImpl) other).permissions);
		}
		return false;
	}
}
