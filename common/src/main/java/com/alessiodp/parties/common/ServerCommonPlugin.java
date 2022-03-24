package com.alessiodp.parties.common;

import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

//pull up member
public class ServerCommonPlugin extends PartiesPlugin{
    public ServerCommonPlugin(ADPBootstrap bootstrap) {
        super(bootstrap);
    }

    @Override
    protected void registerListeners() {

    }

    @Override
    public boolean isBungeeCordEnabled() {
        return false;
    }

    @Override
    public String getServerName(PartyPlayerImpl player) {
        return null;
    }


    @Override
    protected void initializeJsonHandler() {

    }

    @Override
    protected void initializeTitleHandler() {

    }

    @Override
    public int getBstatsId() {
        return 0;
    }

    @Override
    public String getServerId(PartyPlayerImpl player) {
        return getServerName(player);
    }
}
