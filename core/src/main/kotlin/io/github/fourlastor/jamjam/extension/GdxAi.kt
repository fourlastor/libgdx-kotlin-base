package io.github.fourlastor.jamjam.extension

import com.badlogic.gdx.ai.msg.Telegram

interface State<T> : com.badlogic.gdx.ai.fsm.State<T> {
    override fun enter(entity: T) = Unit

    override fun update(entity: T) = Unit

    override fun exit(entity: T) = Unit

    override fun onMessage(entity: T, telegram: Telegram): Boolean = false
}
