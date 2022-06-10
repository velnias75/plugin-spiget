/*
 * Copyright 2022 by Heiko Schäfer <heiko@rangun.de>
 *
 * This file is part of spiget.
 *
 * spiget is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * spiget is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with spiget.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.rangun.spiget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;

import dev.derklaro.spiget.SpigetClient;
import dev.derklaro.spiget.http.java8.Java8SpigetClient;
import dev.derklaro.spiget.mapper.gson.GsonMapper;
import dev.derklaro.spiget.model.Resource;
import dev.derklaro.spiget.model.Version;

public final class PluginClient implements MessageRetriever {

	private final int spigotId;
	private final Logger logger; // NOPMD by heiko on 06.06.22, 06:28

	private final String currentVersion;
	private final String pluginName;

	private final SpigetClient spigetClient = new Java8SpigetClient(GsonMapper.INSTANCE);
	private List<String> joinMessages = new ArrayList<>(3);

	public PluginClient(final int spigotId, final String currentVersion, final String pluginName, final Logger logger) {

		this.spigotId = spigotId;
		this.logger = logger;

		this.currentVersion = currentVersion;
		this.pluginName = pluginName;
	}

	public void checkVersion() {

		try {

			final Version latestVersion = spigetClient.latestResourceVersion().resourceId(spigotId).exec().join();
			final Resource resourceDetails = spigetClient.resourceDetails().resourceId(spigotId).exec().join();

			if (!currentVersion.endsWith("-SNAPSHOT") && !currentVersion.equals(latestVersion.name())) { // NOPMD by
																											// heiko on
																											// 06.06.22,
																											// 06:29

				final String verMsg1 = "A newer version of " + pluginName + " is available: " + latestVersion.name();
				final String verMsg2 = "Download: " + resourceDetails.file().externalUrl();

				logger.warning(verMsg1);
				logger.warning(verMsg2);

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2);

			} else if (currentVersion.endsWith("-SNAPSHOT")) {

				final String verMsg1 = "You are using a development version: " + currentVersion;
				final String verMsg2 = "Latest stable version: " + latestVersion.name();
				final String verMsg3 = "Please report any issues here: "
						+ resourceDetails.links().get("alternativeSupport");

				logger.warning(verMsg1);
				logger.warning(verMsg2);
				logger.warning(verMsg3);

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2);
				joinMessages.add(verMsg3);
			}

		} catch (CompletionException e) {
			logger.warning("Couldn't retrieve latest version.");
		}
	}

	@Override
	public List<String> getJoinMessages() {
		return ImmutableList.copyOf(joinMessages);
	}
}
