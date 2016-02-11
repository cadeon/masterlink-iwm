package org.mlink.agent;

import org.mlink.agent.model.World;

public class WorldConnectionFactory {

	public static WorldConnection getConnection(World world, boolean real) throws Exception {
		if (real) return ProductionWorldConnection.getInstance(world);
		return new SimWorldConnection(world);
	}
}
