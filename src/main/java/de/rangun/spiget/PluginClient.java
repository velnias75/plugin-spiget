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
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;

import dev.derklaro.spiget.SpigetClient;
import dev.derklaro.spiget.http.java8.Java8SpigetClient;
import dev.derklaro.spiget.mapper.gson.GsonMapper;
import dev.derklaro.spiget.model.Resource;
import dev.derklaro.spiget.model.Version;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class PluginClient implements MessageRetriever {

	private final int spigotId;
	private final Logger logger; // NOPMD by heiko on 06.06.22, 06:28

	private final String currentVersion;
	private final String pluginName;

	private final SpigetClient spigetClient = new Java8SpigetClient(GsonMapper.INSTANCE);

	private List<String> joinMessages = new ArrayList<>(3);
	private List<List<TextComponent>> joinComponents = new ArrayList<>(3);

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
				final String url = resourceDetails.file().externalUrl();
				final String verMsg1 = "A newer version of " + pluginName + " is available: " + latestVersion.name();
				final String verMsg2 = "Download: "; // NOPMD by heiko on 16.06.22, 04:35

				logger.warning(verMsg1);
				logger.warning(verMsg2 + url); // NOPMD by heiko on 16.06.22, 04:34

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2 + url);

				final TextComponent urlComponent = new TextComponent(url);
				urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
				urlComponent.setUnderlined(true);

				joinComponents.add(ImmutableList.of(new TextComponent(verMsg1)));
				joinComponents.add(ImmutableList.of(new TextComponent(verMsg2), urlComponent));

			} else if (currentVersion.endsWith("-SNAPSHOT")) {

				final String url = resourceDetails.links().get("alternativeSupport");
				final String verMsg1 = "You are using a development version: " + currentVersion;
				final String verMsg2 = "Latest stable version: " + latestVersion.name();
				final String verMsg3 = "Please report any issues here: "; // NOPMD by heiko on 16.06.22, 04:35

				logger.warning(verMsg1);
				logger.warning(verMsg2);
				logger.warning(verMsg3 + url); // NOPMD by heiko on 16.06.22, 04:36

				joinMessages.add(verMsg1);
				joinMessages.add(verMsg2);
				joinMessages.add(verMsg3 + url);

				joinComponents.add(ImmutableList.of(new TextComponent(verMsg1)));
				joinComponents.add(ImmutableList.of(new TextComponent(verMsg2)));

				final TextComponent urlComponent = new TextComponent(url);
				urlComponent.setUnderlined(true);
				urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

				joinComponents.add(ImmutableList.of(new TextComponent(verMsg3), urlComponent));
			}

		} catch (CompletionException e) {
			logger.warning("Couldn't retrieve latest version.");
		}
	}

	@Override
	public List<String> getJoinMessages() {
		return ImmutableList.copyOf(joinMessages);
	}

	@Override
	public void sendJoinComponents(final Consumer<TextComponent[]> consumer) {

		if (joinComponents.isEmpty()) {
			return;
		}

		final TextComponent pre = new TextComponent("[" + pluginName + ": ");
		pre.setColor(ChatColor.YELLOW);
		pre.setItalic(true);

		final TextComponent suf = new TextComponent("]");
		suf.setColor(ChatColor.YELLOW);
		suf.setItalic(true);

		final List<TextComponent> msgFormatted = new ArrayList<>();
		final List<TextComponent> msg = new ArrayList<>();

		for (final List<TextComponent> jml : joinComponents) {

			msg.add(pre);

			for (final TextComponent jmc : jml) {

				jmc.setColor(ChatColor.YELLOW);
				jmc.setItalic(true);

				msgFormatted.add(jmc);
			}

			msg.addAll(msgFormatted);
			msg.add(suf);

			consumer.accept(msg.toArray(new TextComponent[0])); // NOPMD by heiko on 16.06.22, 05:45

			msgFormatted.clear();
			msg.clear();
		}
	}
}
