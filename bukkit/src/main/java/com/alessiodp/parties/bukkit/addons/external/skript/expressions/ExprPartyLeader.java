package com.alessiodp.parties.bukkit.addons.external.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.Event;

@SuppressWarnings("NullableProblems")
@Name("Party Leader")
@Description("Get the leader as partyplayer of the given party.")
@Examples({"send \"%leader of party with name \"test\"%\"",
		"send \"%leader of event-party%\""})
@Since("3.0.0")
public class ExprPartyLeader extends SimplePropertyExpression<Party, PartyPlayer> {
	static {
		register(ExprPartyLeader.class, PartyPlayer.class, "leader", "party");
	}
	
	@Override
	public Class<? extends PartyPlayer> getReturnType() {
		return PartyPlayer.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "leader";
	}
	
	@Override
	public PartyPlayer convert(Party party) {
		if (party.getLeader() != null)
			return Parties.getApi().getPartyPlayer(party.getLeader());
		return null;
	}
	
	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
		if (delta != null && mode == Changer.ChangeMode.SET) {
			Party party = getExpr().getSingle(e);
			PartyPlayer newLeader = (PartyPlayer) delta[0];
			party.changeLeader(newLeader);
		}
	}
	
	@Override
	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
		return (mode == Changer.ChangeMode.SET) ? CollectionUtils.array(PartyPlayer.class) : null;
	}
}
