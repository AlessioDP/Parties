package com.alessiodp.parties.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.logging.LoggerManager;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.RankManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.utils.MessageUtils;
import lombok.NonNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.internal.util.collections.Sets;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class PartiesPlaceholderTest {
	private static final PartiesPlugin mockPlugin = mock(PartiesPlugin.class);
	private static final PartiesDatabaseManager mockDatabaseManager = mock(PartiesDatabaseManager.class);
	private static final MessageUtils mockMessageUtils = mock(MessageUtils.class);
	private static MockedStatic<ADPPlugin> staticPlugin;
	
	private PartyImpl party1;
	private PartyImpl party2;
	private PartyImpl party3;
	private PartyPlayerImpl player1;
	private PartyPlayerImpl player2;
	private PartyPlayerImpl player3;
	private PartyPlayerImpl player4;
	
	@BeforeAll
	public static void setUp() {
		LoggerManager mockLoggerManager = mock(LoggerManager.class);
		when(mockPlugin.getLoggerManager()).thenReturn(mockLoggerManager);
		when(mockPlugin.getDatabaseManager()).thenReturn(mockDatabaseManager);
		
		when(mockPlugin.getMessageUtils()).thenReturn(mockMessageUtils);
		when(mockMessageUtils.convertRawPlaceholder(any(), any(), any(), any())).thenCallRealMethod();
		
		// Mock names
		OfflineUser mockOfflineUser = mock(OfflineUser.class);
		when(mockPlugin.getOfflinePlayer(any())).thenReturn(mockOfflineUser);
		when(mockOfflineUser.getName()).thenReturn("Dummy");
		
		staticPlugin = mockStatic(ADPPlugin.class);
		when(ADPPlugin.getInstance()).thenReturn(mockPlugin);
	}
	
	@AfterAll
	public static void tearDown() {
		staticPlugin.close();
	}
	
	@BeforeEach
	public void setUpEach() {
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
		
		TestPlayerManager playerManager = spy(new TestPlayerManager(mockPlugin));
		when(mockPlugin.getPlayerManager()).thenReturn(playerManager);
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
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_level_roman");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVEL_ROMAN, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_level_experience");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVEL_EXPERIENCE, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_levelup_necessary");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVELUP_NECESSARY, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("experience_levelup_current");
		assertEquals(PartiesPlaceholder.EXPERIENCE_LEVELUP_CURRENT, placeholder);
	}
	
	@Test
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
	public void testPlaceholderLeaderName() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("leader_name");
		
		assertEquals(PartiesPlaceholder.LEADER_NAME, placeholder);
		assertEquals("Dummy", placeholder.formatPlaceholder(player1, party1, "leader_name"));
	}
	
	@Test
	public void testPlaceholderLeaderUuid() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("leader_uuid");
		
		assertEquals(PartiesPlaceholder.LEADER_UUID, placeholder);
		assertEquals(player1.getPlayerUUID().toString(), placeholder.formatPlaceholder(player1, party1, "leader_uuid"));
	}
	
	@Test
	public void testPlaceholderListPartiesTotal() {
		when(mockDatabaseManager.getListPartiesNumber()).thenReturn(3);
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_parties_total");
		
		assertEquals(PartiesPlaceholder.LIST_PARTIES_TOTAL, placeholder);
		assertEquals("3", placeholder.formatPlaceholder(player1, party1, "list_parties_total"));
	}
	
	@Test
	public void testPlaceholderListPartiesByName() {
		doAnswer(args -> {
			if (((int) args.getArgument(2)) == 0)
				return Sets.newSet(party1);
			else if (((int) args.getArgument(2)) == 1)
				return Sets.newSet(party2);
			else if (((int) args.getArgument(2)) == 2)
				return Sets.newSet(party3);
			return Sets.newSet();
		}).when(mockDatabaseManager).getListParties(eq(PartiesDatabaseManager.ListOrder.NAME), anyInt(), anyInt());
		
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
	public void testPlaceholderListPartiesByOnlineMembers() {
		doAnswer(args -> {
			if (((int) args.getArgument(2)) == 0)
				return Sets.newSet(party3);
			else if (((int) args.getArgument(2)) == 1)
				return Sets.newSet(party2);
			else if (((int) args.getArgument(2)) == 2)
				return Sets.newSet(party1);
			return Sets.newSet();
		}).when(mockDatabaseManager).getListParties(eq(PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS), anyInt(), anyInt());
		
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
	public void testPlaceholderListRank() {
		RankManager mockRankManager = mock(RankManager.class);
		when(mockPlugin.getRankManager()).thenReturn(mockRankManager);
		when(mockRankManager.searchRankByHardName(any())).thenReturn(new PartyRankImpl("myrank", "myrank", "", 20, Collections.emptyList(), true));
		
		when(mockMessageUtils.convertPlaceholders(any(), any(), any())).thenReturn("Dummy");
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("list_rank_myrank");
		assertEquals(PartiesPlaceholder.LIST_RANK, placeholder);
		assertEquals(player1.getName(), placeholder.formatPlaceholder(null, party1, "list_rank_myrank"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_rank_myrank_total");
		assertEquals(PartiesPlaceholder.LIST_RANK_TOTAL, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_rank_myrank_1");
		assertEquals(PartiesPlaceholder.LIST_RANK_NUMBER, placeholder);
		
		placeholder = PartiesPlaceholder.getPlaceholder("list_rank_myrank_1_placeholder");
		assertEquals(PartiesPlaceholder.LIST_RANK_NUMBER_PLACEHOLDER, placeholder);
	}
	
	@Test
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
	public void testPlaceholderName() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("name");
		
		assertEquals(PartiesPlaceholder.NAME, placeholder);
		assertEquals(party1.getName(), placeholder.formatPlaceholder(player1, party1, "name"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("party");
		
		assertEquals(PartiesPlaceholder.PARTY, placeholder);
		assertEquals(party1.getName(), placeholder.formatPlaceholder(player1, party1, "party"));
	}
	
	@Test
	public void testMembers() {
		Messages.PARTIES_LIST_EMPTY = "empty";
		Messages.PARTIES_LIST_ONLINEFORMAT = "player";
		Messages.PARTIES_LIST_OFFLINEFORMAT = "offlineplayer";
		Messages.PARTIES_LIST_SEPARATOR = ",";
		
		when(mockMessageUtils.convertPlaceholders(any(), any(), any())).then(args -> args.getArgument(0));
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("members");
		
		assertEquals(PartiesPlaceholder.MEMBERS, placeholder);
		assertEquals(Messages.PARTIES_LIST_OFFLINEFORMAT + Messages.PARTIES_LIST_SEPARATOR + Messages.PARTIES_LIST_OFFLINEFORMAT, placeholder.formatPlaceholder(player1, party1, "members"));
		
		
	}
	
	@Test
	public void testMembersTotal() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("members_total");
		assertEquals(PartiesPlaceholder.MEMBERS_TOTAL, placeholder);
		assertEquals("2", placeholder.formatPlaceholder(player1, party1, "members_total"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("members_online_total");
		assertEquals(PartiesPlaceholder.MEMBERS_ONLINE_TOTAL, placeholder);
		assertEquals("0", placeholder.formatPlaceholder(player1, party1, "members_online_total"));
		
		placeholder = PartiesPlaceholder.getPlaceholder("members_offline_total");
		assertEquals(PartiesPlaceholder.MEMBERS_OFFLINE_TOTAL, placeholder);
		assertEquals("2", placeholder.formatPlaceholder(player1, party1, "members_offline_total"));
	}
	
	@Test
	public void testPlayerInParty() {
		Messages.PARTIES_OUT_PARTY = "out";
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("out_party");
		
		assertEquals(PartiesPlaceholder.OUT_PARTY, placeholder);
		assertEquals(Messages.PARTIES_OUT_PARTY, placeholder.formatPlaceholder(player1, null, ""));
		assertEquals("", placeholder.formatPlaceholder(player1, party1, ""));
	}
	
	@Test
	public void testPlaceholderTag() {
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("tag");
		
		assertEquals(PartiesPlaceholder.TAG, placeholder);
		assertEquals("", placeholder.formatPlaceholder(player1, party1, ""));
		
		party1.setAccessible(true);
		party1.setTag("tag");
		party1.setAccessible(false);
		assertEquals(party1.getTag(), placeholder.formatPlaceholder(player1, party1, ""));
	}
	
	@Test
	public void testPlaceholderPlayerNickname() {
		ConfigParties.ADDITIONAL_NICKNAME_FORMAT = "~%nickname%";
		
		PartiesPlaceholder placeholder = PartiesPlaceholder.getPlaceholder("player_nickname");
		
		assertEquals(PartiesPlaceholder.PLAYER_NICKNAME, placeholder);
		assertEquals(player1.getName(), placeholder.formatPlaceholder(player1, party1, ""));
		
		player1.setAccessible(true);
		player1.setNickname("newNickname");
		player1.setAccessible(false);
		assertEquals("~" + player1.getNickname(), placeholder.formatPlaceholder(player1, party1, ""));
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
