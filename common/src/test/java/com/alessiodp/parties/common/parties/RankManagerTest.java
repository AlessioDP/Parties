package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.RankManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class RankManagerTest {
	private PartiesPlugin mockPlugin;
	private RankManager rankManager;
	
	private PartyRankImpl rank1;
	private PartyRankImpl rank2;
	private PartyRankImpl rank3;
	
	@Before
	public void setUp() {
		// Ranks
		rank1 = new PartyRankImpl(
				"rank1", "rank1", "rank1", 1,
				Lists.newArrayList(PartiesPermission.PRIVATE_EDIT_COLOR.toString(), PartiesPermission.PRIVATE_EDIT_DESC.toString()), true
		);
		rank2 = new PartyRankImpl(
				"rank2", "rank2", "rank2", 2,
				Lists.newArrayList("-" + PartiesPermission.PRIVATE_EDIT_FOLLOW.toString(), "-" + PartiesPermission.PRIVATE_EDIT_HOME, "*"), false
		);
		rank3 = new PartyRankImpl(
				"rank3", "rank3", "rank3", 3,
				Lists.newArrayList("-" + PartiesPermission.PRIVATE_EDIT_HOME, "*"), false
		);
		
		// Configuration
		ConfigParties.RANK_LIST = Sets.newSet(rank1, rank2, rank3);
		
		mockPlugin = mock(PartiesPlugin.class);
		
		rankManager = new RankManager(mockPlugin);
		rankManager.reload();
	}
	
	@Test
	public void testSearchRankByLevel() {
		assertEquals(rank1, rankManager.searchRankByLevel(1));
		assertEquals(rank2, rankManager.searchRankByLevel(2));
		assertEquals(rank3, rankManager.searchRankByLevel(3));
		
		// Default
		assertEquals(rank1, rankManager.searchRankByLevel(100));
	}
	
	@Test
	public void testSearchRankByName() {
		assertEquals(rank1, rankManager.searchRankByName("rank1"));
		assertEquals(rank2, rankManager.searchRankByName("rank2"));
		assertEquals(rank3, rankManager.searchRankByName("rank3"));
	}
	
	@Test
	public void testSearchRankByHardName() {
		assertEquals(rank1, rankManager.searchRankByHardName("rank1"));
		assertEquals(rank2, rankManager.searchRankByHardName("rank2"));
		assertEquals(rank3, rankManager.searchRankByHardName("rank3"));
	}
	
	@Test
	public void testCheckPlayerRank() {
		PartyPlayerImpl player = mock(PartyPlayerImpl.class);
		
		User user = mock(User.class);
		when(user.hasPermission(anyString())).thenReturn(true);
		when(mockPlugin.getPlayer(any())).thenReturn(user);
		
		// First rank
		when(player.getRank()).thenReturn(1);
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_COLOR));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_DESC));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_FOLLOW));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_HOME));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_MOTD));
		
		// Second rank
		when(player.getRank()).thenReturn(2);
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_COLOR));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_DESC));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_FOLLOW));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_HOME));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_MOTD));
		
		// Third rank
		when(player.getRank()).thenReturn(3);
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_COLOR));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_DESC));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_FOLLOW));
		assertFalse(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_HOME));
		assertTrue(rankManager.checkPlayerRank(player, PartiesPermission.PRIVATE_EDIT_MOTD));
	}
	
	@Test
	public void testCheckPlayerRankAlerter() {
		Messages.PARTIES_PERM_NORANK_UPRANK = "";
		Messages.PARTIES_PERM_NORANK_GENERAL = "";
		
		PartyPlayerImpl player = mock(PartyPlayerImpl.class);
		doNothing().when(player).sendMessage(anyString());
		
		User user = mock(User.class);
		when(user.hasPermission(anyString())).thenReturn(true);
		when(mockPlugin.getPlayer(any())).thenReturn(user);
		
		when(player.getRank()).thenReturn(1);
		assertTrue(rankManager.checkPlayerRankAlerter(player, PartiesPermission.PRIVATE_EDIT_COLOR));
		verify(player, times(0)).sendMessage(anyString());
		assertTrue(rankManager.checkPlayerRankAlerter(player, PartiesPermission.PRIVATE_EDIT_DESC));
		verify(player, times(0)).sendMessage(anyString());
		assertFalse(rankManager.checkPlayerRankAlerter(player, PartiesPermission.PRIVATE_EDIT_FOLLOW));
		verify(player, times(1)).sendMessage(anyString());
		assertFalse(rankManager.checkPlayerRankAlerter(player, PartiesPermission.PRIVATE_EDIT_HOME));
		verify(player, times(2)).sendMessage(anyString());
		assertFalse(rankManager.checkPlayerRankAlerter(player, PartiesPermission.PRIVATE_EDIT_MOTD));
		verify(player, times(3)).sendMessage(anyString());
	}
}
