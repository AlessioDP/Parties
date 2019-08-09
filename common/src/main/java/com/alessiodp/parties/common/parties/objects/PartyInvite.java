package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.core.common.scheduling.CancellableTask;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class PartyInvite {
	@Getter @NonNull private final UUID invitedPlayer;
	@Getter @NonNull private final UUID invitedBy;
	@Getter @NonNull private final CancellableTask task;
}
