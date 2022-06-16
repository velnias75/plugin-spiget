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

import java.util.function.Consumer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author heiko
 *
 */
public interface MessageRetriever {

	/**
	 * <em>Prepare messages for sending to client.</em> <br>
	 * <br>
	 * <b>Example:</b><br>
	 * <br>
	 * <code>private final MessageRetriever msgs;
	 * <br><br>
	 * msgs.sendJoinComponents((msg) -> {
	 *			event.getPlayer().spigot().sendMessage(msg);
	 *		}, ChatColor.YELLOW);</code>
	 * 
	 * @param consumer consumer of prepared messages
	 * @param color    ChatColor of the messages
	 * @throws IllegalArgumentException if color is null
	 */
	void sendJoinComponents(Consumer<TextComponent[]> consumer, ChatColor color);

}
