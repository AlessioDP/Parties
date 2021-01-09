package com.alessiodp.parties.bukkit.addons.external.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

@Name("Add To Party")
@Description("Add the player to the party.")
@Examples({"add player to the party test"})
public class EffAddToParty extends Effect {
	static {
		Skript.registerEffect(EffAddToParty.class,
				"add %offlineplayers% to [the] party [with name] %string%",
				"add %offlineplayers% to [the] party with id %string%");
	}
	
	private Expression<OfflinePlayer> players;
	private Expression<String> party;
	
	private UUID partyId;
	
	@SuppressWarnings({"unchecked"})
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		players = (Expression<OfflinePlayer>) exprs[0];
		if (matchedPattern == 0)
			party = (Expression<String>) exprs[1];
		else
			partyId = UUID.fromString(String.valueOf(exprs[1]));
		return true;
	}
	
	@Override
	protected void execute(Event e) {
		Party p;
		if (partyId != null)
			p = Parties.getApi().getParty(partyId);
		else
			p = Parties.getApi().getParty(party.getSingle(e));
		if (p != null) {
			for (OfflinePlayer player : players.getArray(e)) {
				PartyPlayer partyPlayer = Parties.getApi().getPartyPlayer(player.getUniqueId());
				if (partyPlayer != null) {
					p.addMember(partyPlayer);
				}
			}
		}
	}
	
	@Override
	public String toString(@Nullable Event e, boolean debug) {
		if (partyId != null)
			return "add " + players.toString(e, debug) + " to party with id " + partyId.toString();
		return "add " + players.toString(e, debug) + " to party " + party.toString(e, debug);
	}
}
