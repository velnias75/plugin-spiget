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

import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author heiko
 *
 */
public interface MessageRetriever {

	List<String> getJoinMessages();

	List<List<TextComponent>> getJoinMessages(boolean clickableLinks);
}
