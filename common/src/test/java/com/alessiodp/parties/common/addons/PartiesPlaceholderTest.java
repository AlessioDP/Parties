package com.alessiodp.parties.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.utils.MessageUtils;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.powermock.core.MockRepository;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
public class PartiesPlaceholderTest {
	private PartiesPlugin mockPlugin;
	private PartiesDatabaseManager mockDatabaseManager;
	
	private PartyImpl party1;
	private PartyImpl party2;
	private PartyImpl party3;
	private PartyPlayerImpl player1;
	private PartyPlayerImpl player2;
	private PartyPlayerImpl player3;
	private PartyPlayerImpl player4;
	
	@Before
	public void setUp() {
		MockRepository.clear();
		mockPlugin = mock(PartiesPlugin.class);
		// Mock logger
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		
		// Mock getInstance
		mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
		
		// Mock player manager
		TestPlayerManager playerManager = spy(new TestPlayerManager(mockPlugin));
		when(mockPlugin.getPlayerManager()).thenReturn(playerManager);
		
		// Mock database manager
		mockDatabaseManager = mock(PartiesDatabaseManager.class);
		when(mockPlugin.getDatabaseManager()).thenReturn(mockDatabaseManager);
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		party1 = new TestPartyImpl(mockPlugin, UUID.randomUUID());
		party2 = new TestPartyImpl(mockPlugin, UUID.randomUUID());
		party3 = new TestPartyImpl(mockPlugin, UUID.randomUUID());
		player1 = new TestPartyPlayerImpl(mockPlugin, UUID.randomUUID());
		player2 = new TestPartyPlayerImpl(mockPlugin, UUID.randomUUID());
		player3 = new TestPartyPlayerImpl(mockPlugin, UUID.randomUUID());
		player4 = new TestPartyPlayerImpl(mockPlugin, UUID.randomUUID());
		
		party1.setup("party1", player1.getPlayerUUID().toString());
		party1.getMembers().add(player1.getPlayerUUID());
		player1.setAccessible(true);
		player1.setPartyId(party1.getId());
		player1.setRank(1);
		player1.setAccessible(false);
		
		
		party1.getMembers().add(player2.getPlayerUUID());
		player2.setAccessible(true);
		player2.setPartyId(party1.getId());
		player2.setRank(20);
		player2.setAccessible(false);
		
		party2.setup("party2", player3.getPlayerUUID().toString());
		party2.getMembers().add(player3.getPlayerUUID());
		player3.setAccessible(true);
		player3.setPartyId(party2.getId());
		player3.setRank(20);
		player3.setAccessible(false);
		
		party3.setup("party3", player4.getPlayerUUID().toString());
		party3.getMembers().add(player4.getPlayerUUID());
		player4.setAccessible(true);
		player4.setPartyId(party3.getId());
		player4.setRank(20);
		player4.setAccessible(false);
		
		when(playerManager.getPlayer(any())).then(uuid -> {
			if (uuid.getArgument(0).equals(player1.getPlayerUUID()))
				return player1;
			else if (uuid.getArgument(0).equals(player2.getPlayerUUID()))
				return player2;
			else if (uuid.getArgument(0).equals(player3.getPlayerUUID()))
				return player3;
			else if (uuid.getArgument(0).equals(player4.getPlayerUUID()))
				return player4;
			return null;
		});
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderColorCode() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("color_code");
		
		assertEquals(PartiesPlaceholder.COLOR_CODE, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, "color_code"));
		
		party1.setAccessible(true);
		party1.setColor(new PartyColorImpl("red", "123", "&c", 0, 0, 0));
		party1.setAccessible(false);
		assertEquals(party1.getColor().getCode(), placeholder.formatPlaceholder(player1, party1, "color_code"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderColorCommand() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("color_command");
		
		assertEquals(PartiesPlaceholder.COLOR_COMMAND, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, "color_command"));
		
		party1.setAccessible(true);
		party1.setColor(new PartyColorImpl("red", "123", "&c", 0, 0, 0));
		party1.setAccessible(false);
		assertEquals(party1.getColor().getCommand(), placeholder.formatPlaceholder(player1, party1, "color_command"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderColorName() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("color_name");
		
		assertEquals(PartiesPlaceholder.COLOR_NAME, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, "color_name"));
		
		party1.setAccessible(true);
		party1.setColor(new PartyColorImpl("red", "123", "&c", 0, 0, 0));
		party1.setAccessible(false);
		assertEquals(party1.getColor().getName(), placeholder.formatPlaceholder(player1, party1, "color_name"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderDesc() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("desc");
		
		assertEquals(PartiesPlaceholder.DESC, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, "desc"));
		
		party1.setAccessible(true);
		party1.setDescription("123");
		party1.setAccessible(false);
		assertEquals(party1.getDescription(), placeholder.formatPlaceholder(player1, party1, "desc"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderExperience() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("experience_total");
		assertEquals(PartiesPlaceholder.EXPERIENCE_TOTAL, placeholder);
		assertEquals("0", placeholder.formatPlaceholder(player1, party1, "experience_total"));
		
		party1.setAccessible(true);
		party1.setExperience(100);
		party1.setAccessible(false);
		assertEquals(Integer.toString((int) party1.getExperience()), placeholder.formatPlaceholder(player1, party1, "experience_total"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_level");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVEL, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_level_experience");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVEL_EXPERIENCE, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_levelup_necessary");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVELUP_NECESSARY, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_levelup_current");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVELUP_CURRENT, placeholder);
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderKills() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("kills");
		
		assertEquals(PartiesPlaceholder.KILLS, placeholder);
		assertEquals("0", placeholder.formatPlaceholder(player1, party1, "kills"));
		
		party1.setAccessible(true);
		party1.setKills(100);
		party1.setAccessible(false);
		assertEquals(Integer.toString(party1.getKills()), placeholder.formatPlaceholder(player1, party1, "kills"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderLeaderName() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("leader_name");
		
		assertEquals(PartiesPlaceholder.LEADER_NAME, placeholder);
		assertEquals("Dummy", placeholder.formatPlaceholder(player1, party1, "leader_name"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderLeaderUuid() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("leader_uuid");
		
		assertEquals(PartiesPlaceholder.LEADER_UUID, placeholder);
		assertEquals(player1.getPlayerUUID().toString(), placeholder.formatPlaceholder(player1, party1, "leader_uuid"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderListPartiesTotal() {
		when(mockDatabaseManager.getListPartiesNumber()).thenReturn(3);
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_parties_total");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_TOTAL, placeholder);
		assertEquals("3", placeholder.formatPlaceholder(player1, party1, "list_parties_total"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderListPartiesByName() {
		when(mockDatabaseManager.getListParties(eq(PartiesDatabaseManager.ListOrder.NAME), anyInt(), anyInt())).then(args -> {
			if (((int) args.getArgument(2)) == 0)
				return Sets.newSet(party1);
			else if (((int) args.getArgument(2)) == 1)
				return Sets.newSet(party2);
			else if (((int) args.getArgument(2)) == 2)
				return Sets.newSet(party3);
			return Sets.newSet();
		});
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_name_1");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_NAME_NUMBER, placeholder);
		assertEquals(party1.getName(), placeholder.formatPlaceholder(null, null, "list_parties_by_name_1"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_name_2");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_NAME_NUMBER, placeholder);
		assertEquals(party2.getName(), placeholder.formatPlaceholder(null, null, "list_parties_by_name_2"));
		
		// No more parties
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_name_4");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_NAME_NUMBER, placeholder);
		assertEquals("", placeholder.formatPlaceholder(null, null, "list_parties_by_name_4"));
		
		// Placeholder
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_name_1_id");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_NAME_NUMBER_PLACEHOLDER, placeholder);
		assertEquals(party1.getId().toString(), placeholder.formatPlaceholder(null, null, "list_parties_by_name_1_id"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderListPartiesByOnlineMembers() {
		when(mockDatabaseManager.getListParties(eq(PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS), anyInt(), anyInt())).then(args -> {
			if (((int) args.getArgument(2)) == 0)
				return Sets.newSet(party3);
			else if (((int) args.getArgument(2)) == 1)
				return Sets.newSet(party2);
			else if (((int) args.getArgument(2)) == 2)
				return Sets.newSet(party1);
			return Sets.newSet();
		});
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_online_members_1");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER, placeholder);
		assertEquals(party3.getName(), placeholder.formatPlaceholder(null, null, "list_parties_by_online_members_1"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_online_members_2");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER, placeholder);
		assertEquals(party2.getName(), placeholder.formatPlaceholder(null, null, "list_parties_by_online_members_2"));
		
		// No more parties
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_online_members_4");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER, placeholder);
		assertEquals("", placeholder.formatPlaceholder(null, null, "list_parties_by_online_members_4"));
		
		// Placeholder
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_online_members_1_id");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_ONLINE_MEMBERS_NUMBER_PLACEHOLDER, placeholder);
		assertEquals(party3.getId().toString(), placeholder.formatPlaceholder(null, null, "list_parties_by_online_members_1_id"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderListPartiesByOthers() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_members_1");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_MEMBERS_NUMBER, placeholder);
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_members_1_id");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_MEMBERS_NUMBER_PLACEHOLDER, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_kills_1");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_KILLS_NUMBER, placeholder);
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_kills_1_id");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_KILLS_NUMBER_PLACEHOLDER, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_experience_1");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_EXPERIENCE_NUMBER, placeholder);
		placeholder = PartiesPlaceholder.getPlaceholder("list_parties_by_experience_1_id");
		assertEquals(PartiesPlaceholder.LIST_PARTIES_BY_EXPERIENCE_NUMBER_PLACEHOLDER, placeholder);
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderListPlayersTotal() {
		when(mockDatabaseManager.getListPlayersNumber()).thenReturn(4);
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_players_total");
		
		assertEquals(PartiesPlaceholder.LIST_PLAYERS_TOTAL, placeholder);
		assertEquals("4", placeholder.formatPlaceholder(player1, party1, "list_players_total"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderMotd() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("motd");
		
		assertEquals(PartiesPlaceholder.MOTD, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, "motd"));
		
		party1.setAccessible(true);
		party1.setMotd("motd");
		party1.setAccessible(false);
		assertEquals(party1.getMotd(), placeholder.formatPlaceholder(player1, party1, "motd"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderName() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("name");
		
		assertEquals(PartiesPlaceholder.NAME, placeholder);
		assertEquals(party1.getName(), placeholder.formatPlaceholder(player1, party1, "name"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("party");
		
		assertEquals(PartiesPlaceholder.PARTY, placeholder);
		assertEquals(party1.getName(), placeholder.formatPlaceholder(player1, party1, "party"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testOnline() {
		Messages.PARTIES_LIST_EMPTY = "empty";
		Messages.PARTIES_LIST_ONLINEFORMAT = "player";
		Messages.PARTIES_LIST_SEPARATOR = ",";
		
		MessageUtils messageUtils = mock(MessageUtils.class);
		when(mockPlugin.getMessageUtils()).thenReturn(messageUtils);
		when(messageUtils.convertPlaceholders(any(), any(), any())).then(args -> args.getArgument(0));
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("online");
		
		assertEquals(PartiesPlaceholder.ONLINE, placeholder);
		assertEquals(Messages.PARTIES_LIST_EMPTY, placeholder.formatPlaceholder(player1, party1, "online"));
		
		party1.addOnlineMember(player1);
		assertEquals(Messages.PARTIES_LIST_ONLINEFORMAT, placeholder.formatPlaceholder(player1, party1, "online"));
		
		
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testOnlineNumber() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("online_number");
		
		assertEquals(PartiesPlaceholder.ONLINE_NUMBER, placeholder);
		assertEquals("0", placeholder.formatPlaceholder(player1, party1, "online_number"));
		
		party1.addOnlineMember(player1);
		assertEquals("1", placeholder.formatPlaceholder(player1, party1, "online_number"));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlayerInParty() {
		Messages.PARTIES_OUT_PARTY = "out";
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("out_party");
		
		assertEquals(PartiesPlaceholder.OUT_PARTY, placeholder);
		assertEquals(Messages.PARTIES_OUT_PARTY, placeholder.formatPlaceholder(player1, null, ""));
		assertEquals("", placeholder.formatPlaceholder(player1, party1, ""));
	}
	
	@Test
	@PrepareForTest({ADPPlugin.class, PartiesPlaceholder.class})
	public void testPlaceholderTag() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("tag");
		
		assertEquals(PartiesPlaceholder.TAG, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, ""));
		
		party1.setAccessible(true);
		party1.setTag("tag");
		party1.setAccessible(false);
		assertEquals(party1.getTag(), placeholder.formatPlaceholder(player1, party1, ""));
	}
	
	private static class TestPartyPlayerImpl extends PartyPlayerImpl {
		
		protected TestPartyPlayerImpl(@NonNull PartiesPlugin plugin, @NonNull UUID uuid) {
			super(plugin, uuid);
		}
		
		@Override
		public void playSound(String sound, double volume, double pitch) {
			// Nothing to do
		}
		
		@Override
		public void playChatSound() {
			// Nothing to do
		}
		
		@Override
		public void playBroadcastSound() {
			// Nothing to do
		}
		
		@Override
		public void sendPacketUpdate() {
			// Nothing to do
		}
		
		@Override
		public boolean isVanished() {
			return false;
		}
	}
	
	private static class TestPartyImpl extends PartyImpl {
		
		protected TestPartyImpl(@NonNull PartiesPlugin plugin, @NonNull UUID id) {
			super(plugin, id);
		}
		
		@Override
		public void sendPacketUpdate() {
			// Nothing to do
		}
		
		@Override
		public void sendPacketExperience(double newExperience, PartyPlayer killer) {
			// Nothing to do
		}
		
		@Override
		public void sendPacketLevelUp(int newLevel) {
			// Nothing to do
		}
	}
	
	private static class TestPlayerManager extends PlayerManager {
		
		public TestPlayerManager(PartiesPlugin plugin) {
			super(plugin);
		}
		
		@Override
		public PartyPlayerImpl initializePlayer(UUID playerUUID) {
			return null;
		}
	}
}
