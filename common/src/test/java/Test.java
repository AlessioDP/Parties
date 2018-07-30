import com.alessiodp.parties.api.events.party.PartiesPartyPostDeleteEvent;

public class Test {
	public void test(PartiesPartyPostDeleteEvent event) {
		event.getApi();
		//String a = event.getCommandSender().getPartyName();
	}
}
