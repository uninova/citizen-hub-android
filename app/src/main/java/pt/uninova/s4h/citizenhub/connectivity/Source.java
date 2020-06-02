package pt.uninova.s4h.citizenhub.connectivity;

import java.util.Collection;
import java.util.UUID;

public interface Source {

    UUID[] getChannelIds();

    Channel getChannel(UUID channelId);

}
