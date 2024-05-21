package org.gdsc.yonsei.eaglefilm.manage

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class DiscordConfig(
	@Value("\${discord.token}") private val discordToken: String,
	messageListener: MessageListener
) {
	private var jda: JDA? = null

	init {
		jda = JDABuilder.createDefault(discordToken, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
			.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
			.setBulkDeleteSplittingEnabled(false)
			.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
			.setChunkingFilter(ChunkingFilter.NONE)
			.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
			.setLargeThreshold(50)
			.setAutoReconnect(true)
			.addEventListeners(messageListener)
			.setStatus(OnlineStatus.ONLINE)
			.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
			.build()

		jda?.awaitReady()
	}
}
